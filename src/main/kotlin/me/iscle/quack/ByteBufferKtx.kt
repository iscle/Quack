package me.iscle.quack

import java.nio.ByteBuffer

fun ByteBuffer.getUByte(): UByte {
    return get().toUByte()
}

fun ByteBuffer.getUShort(): UShort {
    return getShort().toUShort()
}

fun ByteBuffer.getUInt(): UInt {
    return getInt().toUInt()
}

fun ByteBuffer.getULong(): ULong {
    return getLong().toULong()
}