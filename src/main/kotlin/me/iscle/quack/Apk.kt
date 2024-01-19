package me.iscle.quack

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.ResourceLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.iscle.quack.manifest.AndroidManifestParser
import me.iscle.quack.manifest.ParsedAndroidManifest
import org.jetbrains.skia.Image
import java.io.File
import java.util.zip.ZipFile

class Apk(
    private val file: File,
) {
    private val zipFile = ZipFile(file)
    var manifest: ParsedAndroidManifest? = null
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
                manifest = zipFile.getInputStream(manifestEntry).use {
                    AndroidManifestParser().parse(it)
                }
            }

            // TODO: Load other stuff
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
}