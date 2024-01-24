package me.iscle.quack.resources

import org.w3c.dom.Document
import java.io.InputStream

interface XmlResourceParser {
    fun parse()
    fun getAsString(
        prettyPrint: Boolean = true,
    ): String
    fun getAsDocument(): Document

    companion object {
        fun fromInputStream(inputStream: InputStream): XmlResourceParser {
            // TODO: check file type and choose parser accordingly
            return BinaryXmlResourceParser(inputStream)
        }
    }
}