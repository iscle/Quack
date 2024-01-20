package me.iscle.quack

import java.io.InputStream
import java.nio.ByteOrder

fun InputStream.readUByte(
    byteOrder: ByteOrder = ByteOrder.nativeOrder(),
): UByte {
    return read().toUByte()
}

fun InputStream.readUShort(
    byteOrder: ByteOrder = ByteOrder.nativeOrder(),
): UShort {
    val b1 = read()
    val b2 = read()
    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> ((b2 shl 8) or b1).toUShort()
        ByteOrder.BIG_ENDIAN -> ((b1 shl 8) or b2).toUShort()
        else -> throw IllegalArgumentException("Unsupported byte order: $byteOrder")
    }
}

fun InputStream.readInt(
    byteOrder: ByteOrder = ByteOrder.nativeOrder(),
): Int {
    val b1 = read()
    val b2 = read()
    val b3 = read()
    val b4 = read()
    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> ((b4 shl 24) or (b3 shl 16) or (b2 shl 8) or b1)
        ByteOrder.BIG_ENDIAN -> ((b1 shl 24) or (b2 shl 16) or (b3 shl 8) or b4)
        else -> throw IllegalArgumentException("Unsupported byte order: $byteOrder")
    }
}

fun InputStream.readUInt(
    byteOrder: ByteOrder = ByteOrder.nativeOrder(),
): UInt {
    val b1 = read()
    val b2 = read()
    val b3 = read()
    val b4 = read()
    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> ((b4 shl 24) or (b3 shl 16) or (b2 shl 8) or b1).toUInt()
        ByteOrder.BIG_ENDIAN -> ((b1 shl 24) or (b2 shl 16) or (b3 shl 8) or b4).toUInt()
        else -> throw IllegalArgumentException("Unsupported byte order: $byteOrder")
    }
}

fun InputStream.readUtf16String(
    byteOrder: ByteOrder = ByteOrder.nativeOrder(),
): String {
    // read until \0
    val bytes = mutableListOf<Byte>()
    val buffer = ByteArray(2)
    while (true) {
        read(buffer)
        if (buffer[0] == 0.toByte() && buffer[1] == 0.toByte()) break
        bytes.add(buffer[0])
        bytes.add(buffer[1])
    }
    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> String(bytes.toByteArray(), Charsets.UTF_16LE)
        ByteOrder.BIG_ENDIAN -> String(bytes.toByteArray(), Charsets.UTF_16BE)
        else -> throw IllegalArgumentException("Unsupported byte order: $byteOrder")
    }
}