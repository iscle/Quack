package me.iscle.quack.manifest

data class ManifestIntentFilter(
    val actions: List<String>,
    val categories: List<String>,
//    val data: List<ManifestData>,
)
