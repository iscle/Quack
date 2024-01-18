package me.iscle.quack

import com.beust.jcommander.Parameter
import com.beust.jcommander.converters.FileConverter
import java.io.File

data class QuackArgs(
    @Parameter(
        description = "The APK file to analyze",
        converter = FileConverter::class,
    )
    var apk: File? = null,
)
