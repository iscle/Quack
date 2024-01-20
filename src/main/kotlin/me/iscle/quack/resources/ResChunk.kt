package me.iscle.quack.resources

import me.iscle.quack.getUInt
import me.iscle.quack.getUShort
import me.iscle.quack.readUInt
import me.iscle.quack.readUShort
import java.io.InputStream
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/libs/androidfw/include/androidfw/ResourceTypes.h

class ResChunk {
    data class Header(
        // Type identifier for this chunk.  The meaning of this value depends
        // on the containing chunk.
        val type: UShort, // uint16_t

        // Size of the chunk header (in bytes).  Adding this value to
        // the address of the chunk allows you to find its associated data
        // (if any).
        val headerSize: UShort, // uint16_t

        // Total size of this chunk (in bytes).  This is the chunkSize plus
        // the size of any data associated with the chunk.  Adding this value
        // to the chunk allows you to completely skip its contents (including
        // any child chunks).  If this value is the same as chunkSize, there is
        // no data associated with the chunk.
        val size: UInt, // uint32_t
    ) {
        override fun toString(): String {
            return "Header(type=$type, headerSize=$headerSize, size=$size)"
        }

        companion object {
            const val RES_NULL_TYPE: UShort = 0x0000u
            const val RES_STRING_POOL_TYPE: UShort = 0x0001u
            const val RES_TABLE_TYPE: UShort = 0x0002u
            const val RES_XML_TYPE: UShort = 0x0003u

            const val RES_XML_FIRST_CHUNK_TYPE: UShort = 0x0100u
            const val RES_XML_START_NAMESPACE_TYPE: UShort = 0x0100u
            const val RES_XML_END_NAMESPACE_TYPE: UShort = 0x0101u
            const val RES_XML_START_ELEMENT_TYPE: UShort = 0x0102u
            const val RES_XML_END_ELEMENT_TYPE: UShort = 0x0103u
            const val RES_XML_CDATA_TYPE: UShort = 0x0104u
            const val RES_XML_LAST_CHUNK_TYPE: UShort = 0x017fu
            // This contains a uint32_t array mapping strings in the string
// pool back to resource identifiers.  It is optional.
            const val RES_XML_RESOURCE_MAP_TYPE: UShort = 0x0180u

            // Chunk types in RES_TABLE_TYPE
            const val RES_TABLE_PACKAGE_TYPE: UShort = 0x0200u
            const val RES_TABLE_TYPE_TYPE: UShort = 0x0201u
            const val RES_TABLE_TYPE_SPEC_TYPE: UShort = 0x0202u
            const val RES_TABLE_LIBRARY_TYPE: UShort = 0x0203u
            const val RES_TABLE_OVERLAYABLE_TYPE: UShort = 0x0204u
            const val RES_TABLE_OVERLAYABLE_POLICY_TYPE: UShort = 0x0205u
            const val RES_TABLE_STAGED_ALIAS_TYPE: UShort = 0x0206u

            fun parse(buffer: ByteBuffer): Header {
                val type = buffer.getUShort()
                val headerSize = buffer.getUShort()
                val size = buffer.getUInt()
                return Header(type, headerSize, size)
            }
        }
    }
}