package me.iscle.quack

import org.slf4j.LoggerFactory
import java.io.File
import java.util.Properties

private const val FALLBACK_LOCALE = "en_US"

object Localization {
    private val logger = LoggerFactory.getLogger(Localization::class.java)

    private val strings = Properties()

    fun init(locale: String) {
        strings.clear()

        var stringsFile = File(getLocalePath(locale))
        if (!stringsFile.exists()) {
            logger.warn("Locale \"$locale\" not found, using fallback locale \"$FALLBACK_LOCALE\"")
            stringsFile = File(getLocalePath(FALLBACK_LOCALE))
        }

        stringsFile.inputStream().use { strings.load(it) }
    }

    fun get(key: String): String {
        return strings.getProperty(key, key)
    }

    private fun getLocalePath(locale: String): String {
        return "src/main/resources/strings/$locale.properties"
    }
}