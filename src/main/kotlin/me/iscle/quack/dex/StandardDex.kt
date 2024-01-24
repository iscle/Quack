package me.iscle.quack.dex

import me.iscle.quack.InputStreamByteBuffer

class StandardDex {

    open class Header(
        val magic: ByteArray,
        val checksum: UInt,
        val signature: ByteArray,
        val fileSize: UInt, // size of entire file
        val headerSize: UInt, // offset to start of next section
        val endianTag: UInt,
        val linkSize: UInt, // unused
        val linkOffset: UInt, // unused
        val mapOffset: UInt, // map list offset from data_off_
        val stringIdsSize: UInt, // number of StringIds
        val stringIdsOffset: UInt, // file offset of StringIds array
        val typeIdsSize: UInt, // number of TypeIds, we don't support more than 65535
        val typeIdsOffset: UInt, // file offset of TypeIds array
        val protoIdsSize: UInt, // number of ProtoIds, we don't support more than 65535
        val protoIdsOffset: UInt, // file offset of ProtoIds array
        val fieldIdsSize: UInt, // number of FieldIds
        val fieldIdsOffset: UInt, // file offset of FieldIds array
        val methodIdsSize: UInt, // number of MethodIds
        val methodIdsOffset: UInt, // file offset of MethodIds array
        val classDefsSize: UInt, // number of ClassDefs
        val classDefsOffset: UInt, // file offset of ClassDef array
        val dataSize: UInt, // size of data section
        val dataOffset: UInt, // file offset of data section
    ) {
        companion object {
            const val DEX_MAGIC_SIZE = 4
            const val DEX_VERSION_LEN = 4
            const val DEX_CONTAINER_VERSION = 41u
            const val SHA1_DIGEST_SIZE = 20
            const val DEX_ENDIAN_CONSTANT = 0x12345678u
            const val DEX_NO_INDEX_16 = 0xFFFFu
            const val DEX_NO_INDEX_32 = 0xFFFFFFFFu

            fun parse(buffer: InputStreamByteBuffer): Header {
                val magic = ByteArray(8).also { buffer.get(it) }
                val checksum = buffer.getUInt()
                val signature = ByteArray(SHA1_DIGEST_SIZE).also { buffer.get(it) }
                val fileSize = buffer.getUInt()
                val headerSize = buffer.getUInt()
                val endianTag = buffer.getUInt()
                val linkSize = buffer.getUInt()
                val linkOffset = buffer.getUInt()
                val mapOffset = buffer.getUInt()
                val stringIdsSize = buffer.getUInt()
                val stringIdsOffset = buffer.getUInt()
                val typeIdsSize = buffer.getUInt()
                val typeIdsOffset = buffer.getUInt()
                val protoIdsSize = buffer.getUInt()
                val protoIdsOffset = buffer.getUInt()
                val fieldIdsSize = buffer.getUInt()
                val fieldIdsOffset = buffer.getUInt()
                val methodIdsSize = buffer.getUInt()
                val methodIdsOffset = buffer.getUInt()
                val classDefsSize = buffer.getUInt()
                val classDefsOffset = buffer.getUInt()
                val dataSize = buffer.getUInt()
                val dataOffset = buffer.getUInt()

                val version = magic.copyOfRange(DEX_MAGIC_SIZE, DEX_MAGIC_SIZE + DEX_VERSION_LEN - 1).toString(Charsets.UTF_8).toUInt()
                return if (version < DEX_CONTAINER_VERSION) {
                    Header(
                        magic = magic,
                        checksum = checksum,
                        signature = signature,
                        fileSize = fileSize,
                        headerSize = headerSize,
                        endianTag = endianTag,
                        linkSize = linkSize,
                        linkOffset = linkOffset,
                        mapOffset = mapOffset,
                        stringIdsSize = stringIdsSize,
                        stringIdsOffset = stringIdsOffset,
                        typeIdsSize = typeIdsSize,
                        typeIdsOffset = typeIdsOffset,
                        protoIdsSize = protoIdsSize,
                        protoIdsOffset = protoIdsOffset,
                        fieldIdsSize = fieldIdsSize,
                        fieldIdsOffset = fieldIdsOffset,
                        methodIdsSize = methodIdsSize,
                        methodIdsOffset = methodIdsOffset,
                        classDefsSize = classDefsSize,
                        classDefsOffset = classDefsOffset,
                        dataSize = dataSize,
                        dataOffset = dataOffset,
                    )
                } else {
                    val containerSize = buffer.getUInt()
                    val headerOffset = buffer.getUInt()

                    HeaderV41(
                        magic = magic,
                        checksum = checksum,
                        signature = signature,
                        fileSize = fileSize,
                        headerSize = headerSize,
                        endianTag = endianTag,
                        linkSize = linkSize,
                        linkOffset = linkOffset,
                        mapOffset = mapOffset,
                        stringIdsSize = stringIdsSize,
                        stringIdsOffset = stringIdsOffset,
                        typeIdsSize = typeIdsSize,
                        typeIdsOffset = typeIdsOffset,
                        protoIdsSize = protoIdsSize,
                        protoIdsOffset = protoIdsOffset,
                        fieldIdsSize = fieldIdsSize,
                        fieldIdsOffset = fieldIdsOffset,
                        methodIdsSize = methodIdsSize,
                        methodIdsOffset = methodIdsOffset,
                        classDefsSize = classDefsSize,
                        classDefsOffset = classDefsOffset,
                        dataSize = dataSize,
                        dataOffset = dataOffset,
                        containerSize = containerSize,
                        headerOffset = headerOffset,
                    )
                }
            }
        }
    }

    class HeaderV41(
        magic: ByteArray,
        checksum: UInt,
        signature: ByteArray,
        fileSize: UInt,
        headerSize: UInt,
        endianTag: UInt,
        linkSize: UInt,
        linkOffset: UInt,
        mapOffset: UInt,
        stringIdsSize: UInt,
        stringIdsOffset: UInt,
        typeIdsSize: UInt,
        typeIdsOffset: UInt,
        protoIdsSize: UInt,
        protoIdsOffset: UInt,
        fieldIdsSize: UInt,
        fieldIdsOffset: UInt,
        methodIdsSize: UInt,
        methodIdsOffset: UInt,
        classDefsSize: UInt,
        classDefsOffset: UInt,
        dataSize: UInt,
        dataOffset: UInt,
        val containerSize: UInt,
        val headerOffset: UInt,
    ) : Header(
        magic = magic,
        checksum = checksum,
        signature = signature,
        fileSize = fileSize,
        headerSize = headerSize,
        endianTag = endianTag,
        linkSize = linkSize,
        linkOffset = linkOffset,
        mapOffset = mapOffset,
        stringIdsSize = stringIdsSize,
        stringIdsOffset = stringIdsOffset,
        typeIdsSize = typeIdsSize,
        typeIdsOffset = typeIdsOffset,
        protoIdsSize = protoIdsSize,
        protoIdsOffset = protoIdsOffset,
        fieldIdsSize = fieldIdsSize,
        fieldIdsOffset = fieldIdsOffset,
        methodIdsSize = methodIdsSize,
        methodIdsOffset = methodIdsOffset,
        classDefsSize = classDefsSize,
        classDefsOffset = classDefsOffset,
        dataSize = dataSize,
        dataOffset = dataOffset,
    )

    companion object {
        val DEX_MAGIC = byteArrayOf(0x64, 0x65, 0x78, 0x0A) // ['d', 'e', 'x', '\n']
    }
}