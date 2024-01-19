package me.iscle.quack

import java.io.InputStream
import java.nio.ByteOrder

// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/libs/androidfw/include/androidfw/ResourceTypes.h

private const val RES_NULL_TYPE = 0x0000u
private const val RES_STRING_POOL_TYPE = 0x0001u
private const val RES_TABLE_TYPE = 0x0002u
private const val RES_XML_TYPE = 0x0003u

private const val RES_XML_FIRST_CHUNK_TYPE = 0x0100u
private const val RES_XML_START_NAMESPACE_TYPE = 0x0100u
private const val RES_XML_END_NAMESPACE_TYPE = 0x0101u
private const val RES_XML_START_ELEMENT_TYPE = 0x0102u
private const val RES_XML_END_ELEMENT_TYPE = 0x0103u
private const val RES_XML_CDATA_TYPE = 0x0104u
private const val RES_XML_LAST_CHUNK_TYPE = 0x017fu
// This contains a uint32_t array mapping strings in the string
// pool back to resource identifiers.  It is optional.
private const val RES_XML_RESOURCE_MAP_TYPE = 0x0180u

// Chunk types in RES_TABLE_TYPE
private const val RES_TABLE_PACKAGE_TYPE = 0x0200u
private const val RES_TABLE_TYPE_TYPE = 0x0201u
private const val RES_TABLE_TYPE_SPEC_TYPE = 0x0202u
private const val RES_TABLE_LIBRARY_TYPE = 0x0203u
private const val RES_TABLE_OVERLAYABLE_TYPE = 0x0204u
private const val RES_TABLE_OVERLAYABLE_POLICY_TYPE = 0x0205u
private const val RES_TABLE_STAGED_ALIAS_TYPE = 0x0206u

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
        companion object {
            fun parse(input: InputStream): Header {
                val type = input.readUShort(ByteOrder.LITTLE_ENDIAN)
                val headerSize = input.readUShort(ByteOrder.LITTLE_ENDIAN)
                val size = input.readUInt(ByteOrder.LITTLE_ENDIAN)
                return Header(type, headerSize, size)
            }
        }
    }
}