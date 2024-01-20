package me.iscle.quack.resources

import me.iscle.quack.*
import java.io.InputStream
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ResXMLTree {

    /**
     * XML tree header.  This appears at the front of an XML tree,
     * describing its content.  It is followed by a flat array of
     * ResXMLTree_node structures; the hierarchy of the XML document
     * is described by the occurrance of RES_XML_START_ELEMENT_TYPE
     * and corresponding RES_XML_END_ELEMENT_TYPE nodes in the array.
     */
    class Header(
        val header: ResChunk.Header,
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): Header {
                val header = ResChunk.Header.parse(buffer)
                return Header(header)
            }
        }
    }

    /**
     * Basic XML tree node.  A single item in the XML document.  Extended info
     * about the node can be found after header.headerSize.
     */
    class Node(
        val header: ResChunk.Header,

        // Line number in original source file at which this element appeared.
        val lineNumber: UInt, // uint32_t

        // Optional XML comment that was associated with this element; -1 if none.
        val comment: Int, // int32_t
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): Node {
                val header = ResChunk.Header.parse(buffer)
                val lineNumber = buffer.getUInt()
                val comment = buffer.getInt()
                return Node(header, lineNumber, comment)
            }
        }
    }

    class CdataExt(
        // The raw CDATA character data.
        val data: ResStringPool.Ref,

        // The typed value of the character data if this is a CDATA node.
        val typedData: Res.Value,
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): CdataExt {
                val data = ResStringPool.Ref.parse(buffer)
                val typedData = Res.Value.parse(buffer)
                return CdataExt(data, typedData)
            }
        }
    }

    class NamespaceExt(
        // The prefix of the namespace.
        val prefix: ResStringPool.Ref,

        // The URI of the namespace.
        val uri: ResStringPool.Ref,
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): NamespaceExt {
                val prefix = ResStringPool.Ref.parse(buffer)
                val uri = ResStringPool.Ref.parse(buffer)
                return NamespaceExt(prefix, uri)
            }
        }
    }

    class EndElementExt(
        // String of the full namespace of this element.
        val ns: ResStringPool.Ref,

        // String name of this node if it is an ELEMENT; the raw
        // character data if this is a CDATA node.
        val name: ResStringPool.Ref,
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): EndElementExt {
                val ns = ResStringPool.Ref.parse(buffer)
                val name = ResStringPool.Ref.parse(buffer)
                return EndElementExt(ns, name)
            }
        }
    }

    class AttrExt(
        // String of the full namespace of this element.
        val ns: ResStringPool.Ref,

        // String name of this node if it is an ELEMENT; the raw
        // character data if this is a CDATA node.
        val name: ResStringPool.Ref,

        // Byte offset from the start of this structure where the attributes start.
        val attributeStart: UShort,

        // Size of the ResXMLTree_attribute structures that follow.
        val attributeSize: UShort,

        // Number of attributes associated with an ELEMENT.  These are
        // available as an array of ResXMLTree_attribute structures
        // immediately following this node.
        val attributeCount: UShort,

        // Index (1-based) of the "id" attribute. 0 if none.
        val idIndex: UShort,

        // Index (1-based) of the "class" attribute. 0 if none.
        val classIndex: UShort,

        // Index (1-based) of the "style" attribute. 0 if none.
        val styleIndex: UShort,
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): AttrExt {
                val ns = ResStringPool.Ref.parse(buffer)
                val name = ResStringPool.Ref.parse(buffer)
                val attributeStart = buffer.getUShort()
                val attributeSize = buffer.getUShort()
                val attributeCount = buffer.getUShort()
                val idIndex = buffer.getUShort()
                val classIndex = buffer.getUShort()
                val styleIndex = buffer.getUShort()
                return AttrExt(ns, name, attributeStart, attributeSize, attributeCount, idIndex, classIndex, styleIndex)
            }
        }
    }

    class Attribute(
        // Namespace of this attribute.
        val ns: ResStringPool.Ref,

        // Name of this attribute.
        val name: ResStringPool.Ref,

        // The original raw string value of this attribute.
        val rawValue: ResStringPool.Ref,

        // Processesd typed value of this attribute.
        val typedValue: Res.Value,
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): Attribute {
                val ns = ResStringPool.Ref.parse(buffer)
                val name = ResStringPool.Ref.parse(buffer)
                val rawValue = ResStringPool.Ref.parse(buffer)
                val typedValue = Res.Value.parse(buffer)
                return Attribute(ns, name, rawValue, typedValue)
            }
        }
    }
}