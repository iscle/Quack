package me.iscle.quack.resources

import me.iscle.quack.getUInt
import me.iscle.quack.readUInt
import me.iscle.quack.readUtf16String
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ResTable {

    class Header(
        val header: ResChunk.Header,

        // The number of ResTable_package structures.
        val packageCount: UInt, // uint32_t
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): Header {
                val header = ResChunk.Header.parse(buffer)
                val packageCount = buffer.getUInt()
                return Header(header, packageCount)
            }
        }
    }

    class Package(
        header: ResChunk.Header,

        // If this is a base package, its ID.  Package IDs start
        // at 1 (corresponding to the value of the package bits in a
        // resource identifier).  0 means this is not a base package.
        val id: UInt, // uint32_t

        // Actual name of this package, \0-terminated.
        val name: String,

        // Offset to a ResStringPool_header defining the resource
        // type symbol table.  If zero, this package is inheriting from
        // another base package (overriding specific values in it).
        val typeStrings: UInt, // uint32_t

        // Last index into typeStrings that is for public use by others.
        val lastPublicType: UInt, // uint32_t

        // Offset to a ResStringPool_header defining the resource
        // key symbol table.  If zero, this package is inheriting from
        // another base package (overriding specific values in it).
        val keyStrings: UInt, // uint32_t

        // Last index into keyStrings that is for public use by others.
        val lastPublicKey: UInt, // uint32_t

        typeIdOffset: UInt, // uint32_t
    ) {
        companion object {
            fun parse(buffer: ByteBuffer): Package {
                val header = ResChunk.Header.parse(buffer)
                val id = buffer.getUInt()
                val name = "buffer.getUtf16String()" // TODO
                val typeStrings = buffer.getUInt()
                val lastPublicType = buffer.getUInt()
                val keyStrings = buffer.getUInt()
                val lastPublicKey = buffer.getUInt()
                val typeIdOffset = buffer.getUInt()
                return Package(header, id, name, typeStrings, lastPublicType, keyStrings, lastPublicKey, typeIdOffset)
            }
        }
    }
}