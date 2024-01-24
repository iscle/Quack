package me.iscle.quack.dex

import me.iscle.quack.InputStreamByteBuffer
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.nio.ByteOrder

class DexParser(
    inputStream: InputStream,
) {
    private val logger = LoggerFactory.getLogger(DexParser::class.java)

    private val buffer = InputStreamByteBuffer(
        input = inputStream,
        order = ByteOrder.LITTLE_ENDIAN,
    )

    private var stringIds: UIntArray? = null
    private var typeIds: UIntArray? = null
    private var protoIds: UIntArray? = null

    @OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
    fun parse() {
        val header = StandardDex.Header.parse(buffer)

        val magic = header.magic.copyOfRange(0, StandardDex.Header.DEX_MAGIC_SIZE)
        if (!magic.contentEquals(StandardDex.DEX_MAGIC)) {
            logger.warn("Invalid dex magic, expected ${StandardDex.DEX_MAGIC}, got $magic")
        }

        if (header.endianTag != StandardDex.Header.DEX_ENDIAN_CONSTANT) {
            logger.warn("Invalid dex endian tag, expected ${StandardDex.Header.DEX_ENDIAN_CONSTANT.toHexString()}, got ${header.endianTag.toHexString()}")
        }

        if (header.stringIdsSize != 0u) {
            buffer.position(header.stringIdsOffset.toInt())
            stringIds = UIntArray((header.stringIdsSize / 4u).toInt())
            buffer.get(stringIds!!)
        }

        if (header.typeIdsSize != 0u) {
            buffer.position(header.typeIdsOffset.toInt())
            typeIds = UIntArray((header.typeIdsSize / 4u).toInt())
            buffer.get(typeIds!!)
        }

        if (header.protoIdsSize != 0u) {
            buffer.position(header.protoIdsOffset.toInt())
            // TODO: Parse proto ids
        }

        if (header.fieldIdsSize != 0u) {
            buffer.position(header.fieldIdsOffset.toInt())
            // TODO: Parse field ids
        }

        if (header.methodIdsSize != 0u) {
            buffer.position(header.methodIdsOffset.toInt())
            // TODO: Parse method ids
        }

        if (header.classDefsSize != 0u) {
            buffer.position(header.classDefsOffset.toInt())
            // TODO: Parse class defs
        }

        if (header.dataSize != 0u) {
            buffer.position(header.dataOffset.toInt())
            // TODO: Parse data
        }
    }
}