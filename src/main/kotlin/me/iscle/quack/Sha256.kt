package me.iscle.quack

import java.io.File
import java.security.MessageDigest

fun File.sha256(): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")

    inputStream().use { fis ->
        val buffer = ByteArray(8192)
        while (true) {
            val read = fis.read(buffer)
            if (read == -1) break
            digest.update(buffer, 0, read)
        }
    }

    return digest.digest()
}