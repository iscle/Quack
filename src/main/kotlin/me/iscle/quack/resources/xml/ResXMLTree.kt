package me.iscle.quack.resources.xml

import me.iscle.quack.ResChunk
import me.iscle.quack.readInt
import me.iscle.quack.readUInt
import java.io.InputStream
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
            fun parse(input: InputStream): Header {
                val header = ResChunk.Header.parse(input)
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
            fun parse(input: InputStream): Node {
                val header = ResChunk.Header.parse(input)
                val lineNumber = input.readUInt(ByteOrder.LITTLE_ENDIAN)
                val comment = input.readInt(ByteOrder.LITTLE_ENDIAN)
                return Node(header, lineNumber, comment)
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
            fun parse(input: InputStream): Attribute {
                val ns = ResStringPool.Ref.parse(input)
                val name = ResStringPool.Ref.parse(input)
                val rawValue = ResStringPool.Ref.parse(input)
                val typedValue = Res.Value.parse(input)
                return Attribute(ns, name, rawValue, typedValue)
            }
        }
    }
}