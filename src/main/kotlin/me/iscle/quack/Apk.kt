package me.iscle.quack

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.ResourceLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.iscle.quack.manifest.AndroidManifest
import me.iscle.quack.manifest.AndroidManifestParser
import org.jetbrains.skia.Image
import java.io.File
import java.util.zip.ZipFile

private const val APK_RESOURCE_TABLE_PATH = "resources.arsc"
private const val PROTO_RESOURCE_TABLE_PATH = "resources.pb"
private const val ANDROID_MANIFEST_PATH = "AndroidManifest.xml"

class Apk(
    private val file: File,
) {
    private val zipFile = ZipFile(file)
    var manifest: AndroidManifest? = null
        private set

    var stringManifest: String? = null
        private set

    companion object {
        fun isValid(file: File): Boolean {
            return try {
                ZipFile(file).use { true }
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun load() {
        withContext(Dispatchers.IO) {
            // Load manifest
            val manifestEntry = zipFile.getEntry("AndroidManifest.xml")
            if (manifestEntry != null) {
                zipFile.getInputStream(manifestEntry).use {
                    val parser = AndroidManifestParser(it)
                    manifest = parser.getAsAndroidManifest()
                    stringManifest = parser.getAsString()
                }
            }

            // TODO: Load other stuff
//            val classesDex = zipFile.getEntry("classes.dex")
//            if (classesDex != null) {
//                zipFile.getInputStream(classesDex).use {
//                    DexParser(it).parse()
//                }
//            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun getIcon(): Image {
        val fallbackIcon = ResourceLoader.Default.load("icon_1.jpeg").readAllBytes()
        return Image.makeFromEncoded(fallbackIcon)
    }

    fun getLabel(): String {
        return "Adif"
    }

    enum class Format {
        Unknown,
        Binary,
        Proto,
    }
}