package me.iscle.quack

import me.iscle.quack.resources.ResChunk
import me.iscle.quack.resources.ResStringPool
import me.iscle.quack.resources.ResXMLTree
import org.slf4j.LoggerFactory
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.InputStream
import java.io.StringWriter
import java.nio.ByteOrder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class BinaryXmlResourceParser(
    input: InputStream,
) {
    private val logger = LoggerFactory.getLogger(BinaryXmlResourceParser::class.java)

    private val buffer = InputStreamByteBuffer(
        input = input,
        order = ByteOrder.LITTLE_ENDIAN
    )

    private val strings = mutableListOf<String>()
    private val styles = mutableListOf<UInt>()
    private var resourceIds: UIntArray? = null

    private val document = DocumentBuilderFactory.newInstance().apply {
        isNamespaceAware = true
    }.newDocumentBuilder().newDocument()
    private val namespaces = mutableMapOf<String, String>()
    private val nodeStack = mutableListOf<Node>(document)

    private var parsed = false

    fun parse(): String {
        if (parsed) throw Exception("Already parsed")
        parsed = true

        val header = ResChunk.Header.parse(buffer)
        buffer.readBytes((header.size - header.headerSize).toInt())
        while (buffer.position() < header.size.toInt()) {
            buffer.mark()
            val currentPosition = buffer.position()
            val chunkHeader = ResChunk.Header.parse(buffer)
            val nextPosition = currentPosition + chunkHeader.size.toInt()

            when {
                chunkHeader.type == ResChunk.Header.RES_STRING_POOL_TYPE -> parseResStringPoolType()
                chunkHeader.type == ResChunk.Header.RES_XML_RESOURCE_MAP_TYPE -> parseResXmlResourceMapType(chunkHeader)
                chunkHeader.type >= ResChunk.Header.RES_XML_FIRST_CHUNK_TYPE
                        && chunkHeader.type <= ResChunk.Header.RES_XML_LAST_CHUNK_TYPE -> parseResXmlChunkType()
                else -> {
                    logger.warn("Unknown chunk type: ${chunkHeader.type}")
                }
            }

            if (buffer.position() != nextPosition) {
                logger.warn("Expected position $nextPosition but got ${buffer.position()}")
                buffer.position(nextPosition)
            }
        }
        logger.info("Finished parsing binary XML resource file")

        val transformerFactory = TransformerFactory.newInstance();
        val transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        val writer = StringWriter();
        transformer.transform(DOMSource(document), StreamResult(writer));
        return writer.toString()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun parseResStringPoolType() {
        buffer.reset()
        val startPosition = buffer.position()
        val header = ResStringPool.Header.parse(buffer)

        val stringIndices = UIntArray(header.stringCount.toInt())
        buffer.get(stringIndices) // not used for now, but we might need it in the future

        val styleIndices = UIntArray(header.styleCount.toInt())
        buffer.get(styleIndices) // not used for now, but we might need it in the future

        assert(buffer.position() == startPosition + header.stringsStart.toInt())

        val isUtf8 = header.flags and ResStringPool.Header.UTF8_FLAG == ResStringPool.Header.UTF8_FLAG
        if (isUtf8) {
            for (i in 0 until header.stringCount.toInt()) {
                val stringLength = buffer.getUShort()
                val string = buffer.getUtf8String(stringLength.toInt(), true)
                strings.add(string)
            }
        } else {
            for (i in 0 until header.stringCount.toInt()) {
                val stringLength = buffer.getUShort()
                val string = buffer.getUtf16String(stringLength.toInt(), true)
                strings.add(string)
            }
        }

        assert(buffer.position() == startPosition + header.stylesStart.toInt())

        for (i in 0 until header.styleCount.toInt()) {
            val style = buffer.getUInt()
            styles.add(style)
        }

        logger.info("Parsed string pool with ${header.stringCount} strings and ${header.styleCount} styles")
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun parseResXmlResourceMapType(header: ResChunk.Header) {
        val numResIds = (header.size - header.headerSize) / 4u // uint32_t
        resourceIds = UIntArray(numResIds.toInt())
        buffer.get(resourceIds!!)
    }

    private fun parseResXmlChunkType() {
        buffer.reset()
        val node = ResXMLTree.Node.parse(buffer)
//        logger.debug("Line number: ${node.lineNumber}, comment: ${node.comment}")
        when (node.header.type) {
            ResChunk.Header.RES_XML_START_NAMESPACE_TYPE -> {
                val namespaceExt = ResXMLTree.NamespaceExt.parse(buffer)
                val prefix = getString(namespaceExt.prefix.index.toInt())
                val uri = getString(namespaceExt.uri.index.toInt())

                if (prefix == null || uri == null) {
                    logger.warn("Invalid namespace: $prefix, $uri")
                    return
                }

                namespaces[uri] = prefix
            }
            ResChunk.Header.RES_XML_END_NAMESPACE_TYPE -> {
                val namespaceExt = ResXMLTree.NamespaceExt.parse(buffer)
                val prefix = getString(namespaceExt.prefix.index.toInt())
                val uri = getString(namespaceExt.uri.index.toInt())

                if (prefix == null || uri == null) {
                    logger.warn("Invalid namespace: $prefix, $uri")
                    return
                }
            }
            ResChunk.Header.RES_XML_START_ELEMENT_TYPE -> {
                val attrExt = ResXMLTree.AttrExt.parse(buffer)
                val ns = getString(attrExt.ns.index.toInt())
                val name = getString(attrExt.name.index.toInt())

                if (name == "uses-sdk") {
                    logger.debug("uses-sdk")
                }

                val element = document.createElementNS(ns, name)

                for (i in 0 until attrExt.attributeCount.toInt()) {
                    val attr = ResXMLTree.Attribute.parse(buffer)
                    val attrNs = getString(attr.ns.index.toInt())
                    val attrName = getString(attr.name.index.toInt())
                    val attrRawValue = getString(attr.rawValue.index.toInt())
                    if (attrRawValue == null) {
                        // We have no interest in attributes without a value
                        continue
                    }

                    element.setAttributeNodeNS(document.createAttributeNS(attrNs, attrName).apply {
                        prefix = namespaces[attrNs]
                        value = attrRawValue
                    })
                }

                nodeStack.last().appendChild(element)
                nodeStack.add(element)
            }
            ResChunk.Header.RES_XML_END_ELEMENT_TYPE -> {
                val endElementExt = ResXMLTree.EndElementExt.parse(buffer)
                val ns = getString(endElementExt.ns.index.toInt())
                val name = getString(endElementExt.name.index.toInt())

                val node = nodeStack.removeLast()
                if (node.nodeName != name || node.namespaceURI != ns) {
                    logger.warn("End element $ns:$name does not match start element ${node.namespaceURI}:${node.nodeName}")
                }
            }
            ResChunk.Header.RES_XML_CDATA_TYPE -> {
                val cdataExt = ResXMLTree.CdataExt.parse(buffer)
                val data = getString(cdataExt.data.index.toInt())
                logger.debug("CDATA: $data")
            }
            else -> {
                logger.warn("Unknown ResXMLTree node type: ${node.header.type}")
            }
        }
    }

    private fun getString(index: Int): String? {
        return strings.getOrNull(index)
    }
}