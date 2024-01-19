package me.iscle.quack

import java.io.InputStream

class BinaryXmlResourceParser {
    fun parse(input: InputStream) {
        val header = ResChunk.Header.parse(input)
        
    }
}