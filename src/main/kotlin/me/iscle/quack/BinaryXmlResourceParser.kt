package me.iscle.quack

import me.iscle.quack.resources.ResChunk
import me.iscle.quack.resources.ResStringPool
import me.iscle.quack.resources.ResXMLTree
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BinaryXmlResourceParser {
    fun parse(input: InputStream) {
        val buffer = let {
            val headerBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
            if (input.read(headerBuffer.array()) != headerBuffer.capacity()) {
                throw Exception("Failed to read XML header")
            }
            val header = ResXMLTree.Header.parse(headerBuffer)
            if (header.header.type != ResChunk.Header.RES_XML_TYPE
                || header.header.headerSize != 8.toUShort()) {
                throw Exception("Invalid XML header")
            }

            val buffer = ByteBuffer.allocate(header.header.size.toInt()).order(ByteOrder.LITTLE_ENDIAN)
            System.arraycopy(headerBuffer.array(), 0, buffer.array(), 0, headerBuffer.capacity())
            if (input.read(buffer.array(), headerBuffer.capacity(), buffer.capacity() - headerBuffer.capacity()) != buffer.capacity() - headerBuffer.capacity()) {
                throw Exception("Failed to read XML")
            }
            buffer
        }

        val header = ResXMLTree.Header.parse(buffer)
        while (buffer.position() < header.header.size.toInt()) {
            val chunkHeader = ResChunk.Header.parse(buffer)
            buffer.position(buffer.position() - 8)
            val nextPos = buffer.position() + chunkHeader.size.toInt()

            when {
                chunkHeader.type == ResChunk.Header.RES_STRING_POOL_TYPE -> {
                    buffer.position(buffer.position() - 8)
                    val stringPoolHeader = ResStringPool.Header.parse(buffer)
                    println(stringPoolHeader.toString())
                }
                chunkHeader.type == ResChunk.Header.RES_XML_RESOURCE_MAP_TYPE -> {
                    println("RES_XML_RESOURCE_MAP_TYPE")
                }
                chunkHeader.type >= ResChunk.Header.RES_XML_FIRST_CHUNK_TYPE
                        && chunkHeader.type <= ResChunk.Header.RES_XML_LAST_CHUNK_TYPE -> {
                    println("RES_XML_*_TYPE")
                }
            }

            buffer.position(nextPos)
        }
        println("Finished parsing XML")
    }
}