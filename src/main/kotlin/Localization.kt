import java.io.File
import java.util.Properties

private const val FALLBACK_LOCALE = "en_US"

object Localization {
    private val strings = Properties()

    fun init(locale: String) {
        strings.clear()

        var stringsFile = File("src/main/resources/strings/$locale.properties")
        if (!stringsFile.exists()) {
            println("Locale \"$locale\" not found, using fallback locale \"$FALLBACK_LOCALE\"")
            stringsFile = File("src/main/resources/strings/$FALLBACK_LOCALE.properties")
        }

        stringsFile.inputStream().use { strings.load(it) }
    }

    fun get(key: String): String {
        return strings.getProperty(key, key)
    }
}