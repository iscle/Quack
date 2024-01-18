package me.iscle.quack

import java.io.File
import java.security.MessageDigest

object Sha256 {
    @OptIn(ExperimentalStdlibApi::class)
    fun ofFile(file: File): String {
        return file.inputStream().use { fis ->
            val digest = MessageDigest.getInstance("SHA-256")
            val buffer = ByteArray(8192)
            while (true) {
                val read = fis.read(buffer)
                if (read == -1) break
                digest.update(buffer, 0, read)
            }
            digest.digest().toHexString()
        }
    }
}