package me.iscle.quack

import java.io.InputStream
import java.nio.*

class InputStreamByteBuffer(
    private val input: InputStream,
    initialCapacity: Int = 8,
    order: ByteOrder = ByteOrder.nativeOrder()
) {
    private var buffer = ByteBuffer.allocate(initialCapacity).order(order)

    init {
        buffer.limit(0)
    }

    private fun requireBytes(count: Int) {
        // If we have enough bytes, return
        if (buffer.remaining() >= count) return

        // If we don't have enough bytes, read more
        val bytesToRead = count - buffer.remaining()
        readBytes(bytesToRead)
    }

    private fun requireLimit(count: Int) {
        if (buffer.limit() < count) {
            requireBytes(count - buffer.limit())
        }
    }

    fun readBytes(count: Int) {
        if (buffer.capacity() - buffer.limit() < count) {
            var newCapacity = buffer.capacity()
            while (newCapacity - buffer.limit() < count) {
                newCapacity *= 2
            }
            val newBuffer = ByteBuffer.allocate(newCapacity).order(buffer.order())
            buffer.flip()
            newBuffer.put(buffer)
            newBuffer.limit(buffer.limit())
            buffer = newBuffer
        }

        var totalBytesRead = 0
        while (true) {
            val bytesRead = input.read(buffer.array(), buffer.limit(), count - totalBytesRead)
            if (bytesRead == -1) {
                throw Exception("Failed to read $count bytes")
            }
            totalBytesRead += bytesRead
            if (totalBytesRead == count) break
        }
        buffer.limit(buffer.limit() + count)
    }

    fun position(): Int {
        return buffer.position()
    }

    fun position(position: Int) {
        requireLimit(position)
        buffer.position(position)
    }

    fun mark() {
        buffer.mark()
    }

    fun reset() {
        buffer.reset()
    }

    fun getByte(): Byte {
        requireBytes(1)
        return buffer.get()
    }

    fun get(array: ByteArray) {
        requireBytes(array.size)
        buffer.get(array)
    }

    fun getUByte(): UByte {
        requireBytes(1)
        return buffer.get().toUByte()
    }

    fun getShort(): Short {
        requireBytes(2)
        return buffer.short
    }

    fun get(array: ShortArray) {
        requireBytes(array.size * 2)
        buffer.asShortBuffer().get(array)
        buffer.position(buffer.position() + array.size * 2)
    }

    fun getUShort(): UShort {
        requireBytes(2)
        return buffer.short.toUShort()
    }

    fun getInt(): Int {
        requireBytes(4)
        return buffer.int
    }

    fun get(array: IntArray) {
        requireBytes(array.size * 4)
        buffer.asIntBuffer().get(array)
        buffer.position(buffer.position() + array.size * 4)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun get(array: UIntArray) {
        get(array.asIntArray())
    }

    fun getUInt(): UInt {
        requireBytes(4)
        return buffer.int.toUInt()
    }

    fun getLong(): Long {
        requireBytes(8)
        return buffer.long
    }

    fun get(array: LongArray) {
        requireBytes(array.size * 8)
        buffer.asLongBuffer().get(array)
        buffer.position(buffer.position() + array.size * 8)
    }

    fun getULong(): ULong {
        requireBytes(8)
        return buffer.long.toULong()
    }

    fun getUtf8String(
        length: Int,
        consumeNullTerminator: Boolean,
    ): String {
        val buf = ByteArray(length)
        get(buf)
        if (consumeNullTerminator) {
            getByte()
        }
        return String(buf, Charsets.UTF_8)
    }

    fun getUtf16String(
        length: Int,
        consumeNullTerminator: Boolean,
    ): String {
        val buf = ByteArray(length * 2)
        get(buf)
        if (consumeNullTerminator) {
            getShort()
        }
        return when (buffer.order()) {
            ByteOrder.LITTLE_ENDIAN -> String(buf, Charsets.UTF_16LE)
            ByteOrder.BIG_ENDIAN -> String(buf, Charsets.UTF_16BE)
            else -> throw IllegalArgumentException("Unsupported byte order: ${buffer.order()}")
        }
    }
}