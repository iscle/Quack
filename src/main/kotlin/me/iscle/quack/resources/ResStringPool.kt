package me.iscle.quack.resources

import me.iscle.quack.InputStreamByteBuffer

class ResStringPool {

    class Ref(
        // Index into the string pool table (uint32_t-offset from the indices
        // immediately after ResStringPool_header) at which to find the location
        // of the string data in the pool.
        val index: UInt, // uint32_t
    ) {
        companion object {
            fun parse(buffer: InputStreamByteBuffer): Ref {
                val index = buffer.getUInt()
                return Ref(index)
            }
        }
    }

    class Header(
        val header: ResChunk.Header,

        // Number of strings in this pool (number of uint32_t indices that follow
        // in the data).
        val stringCount: UInt, // uint32_t

        // Number of style span arrays in the pool (number of uint32_t indices
        // follow the string indices).
        val styleCount: UInt, // uint32_t

        // Flags.
        val flags: UInt, // uint32_t

        // Index from header of the string data.
        val stringsStart: UInt, // uint32_t

        // Index from header of the style data.
        val stylesStart: UInt, // uint32_t
    ) {
        override fun toString(): String {
            return "Header(header=$header, stringCount=$stringCount, styleCount=$styleCount, flags=$flags, stringsStart=$stringsStart, stylesStart=$stylesStart)"
        }

        companion object {
            // If set, the string index is sorted by the string values (based
            // on strcmp16()).
            val SORTED_FLAG = 1u shl 0

            // String pool is encoded in UTF-8
            val UTF8_FLAG = 1u shl 8

            fun parse(buffer: InputStreamByteBuffer): Header {
                val header = ResChunk.Header.parse(buffer)
                val stringCount = buffer.getUInt()
                val styleCount = buffer.getUInt()
                val flags = buffer.getUInt()
                val stringsStart = buffer.getUInt()
                val stylesStart = buffer.getUInt()
                return Header(header, stringCount, styleCount, flags, stringsStart, stylesStart)
            }
        }
    }

    class Span(
        // This is the name of the span -- that is, the name of the XML
        // tag that defined it.  The special value END (0xFFFFFFFF) indicates
        // the end of an array of spans.
        val name: Ref,

        // The range of characters in the string that this span applies to.
        val firstChar: UInt, // uint32_t
        val lastChar: UInt, // uint32_t
    ) {
        companion object {
            const val END = 0xFFFFFFFF

            fun parse(buffer: InputStreamByteBuffer): Span {
                val name = Ref.parse(buffer)
                val firstChar = buffer.getUInt()
                val lastChar = buffer.getUInt()
                return Span(name, firstChar, lastChar)
            }
        }
    }
}