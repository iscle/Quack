package me.iscle.quack

import me.iscle.quack.resources.ResChunk
import me.iscle.quack.resources.ResStringPool
import me.iscle.quack.resources.ResXMLTree
import java.io.InputStream
import java.nio.ByteOrder

class BinaryXmlResourceParser {
    fun parse(input: InputStream) {
        val buffer = InputStreamByteBuffer(
            input = input,
            order = ByteOrder.LITTLE_ENDIAN
        )

        val header = ResChunk.Header.parse(buffer)
        buffer.readBytes((header.size - header.headerSize).toInt())
        while (buffer.position() < header.size.toInt()) {
            val currentPosition = buffer.position()
            buffer.mark()
            val chunkHeader = ResChunk.Header.parse(buffer)
            val nextPosition = currentPosition + chunkHeader.size.toInt()

            when {
                chunkHeader.type == ResChunk.Header.RES_STRING_POOL_TYPE -> {
                    buffer.reset()
                    val stringPoolHeader = ResStringPool.Header.parse(buffer)

                    val isUtf8 = stringPoolHeader.flags and ResStringPool.Header.UTF8_FLAG == ResStringPool.Header.UTF8_FLAG
                    val charSize = if (isUtf8) 1u else 2u
                    var stringPoolSize = if (stringPoolHeader.styleCount == 0u) {
                        stringPoolHeader.header.size - stringPoolHeader.stringsStart
                    } else {
                        stringPoolHeader.stylesStart - stringPoolHeader.stringsStart
                    } / charSize
                    println("String pool size: $stringPoolSize")
                }
                chunkHeader.type == ResChunk.Header.RES_XML_RESOURCE_MAP_TYPE -> {
                    val numResIds = (chunkHeader.size - chunkHeader.headerSize) / 4u // uint32_t
                    val resIds = IntArray(numResIds.toInt())
                    buffer.get(resIds)
                }
                chunkHeader.type >= ResChunk.Header.RES_XML_FIRST_CHUNK_TYPE
                        && chunkHeader.type <= ResChunk.Header.RES_XML_LAST_CHUNK_TYPE -> {
                    buffer.reset()
                    val resXmlTreeNode = ResXMLTree.Node.parse(buffer)
                    when (resXmlTreeNode.header.type) {
                        ResChunk.Header.RES_XML_START_NAMESPACE_TYPE -> {
                            val namespaceExt = ResXMLTree.NamespaceExt.parse(buffer)
                            println(namespaceExt)
                        }
                        ResChunk.Header.RES_XML_END_NAMESPACE_TYPE -> {
                            val namespaceExt = ResXMLTree.NamespaceExt.parse(buffer)
                            println(namespaceExt)
                        }
                        ResChunk.Header.RES_XML_START_ELEMENT_TYPE -> {
                            val attrExt = ResXMLTree.AttrExt.parse(buffer)
                            println(attrExt)
                        }
                        ResChunk.Header.RES_XML_END_ELEMENT_TYPE -> {
                            val endElementExt = ResXMLTree.EndElementExt.parse(buffer)
                            println(endElementExt)
                        }
                        ResChunk.Header.RES_XML_CDATA_TYPE -> {
                            val cdataExt = ResXMLTree.CdataExt.parse(buffer)
                            println(cdataExt)
                        }
                    }
                }
            }

            if (buffer.position() != nextPosition) {
                println("WARNING: Expected position $nextPosition but got ${buffer.position()}")
                buffer.position(nextPosition)
            }
        }
        println("Finished parsing XML")
    }
}