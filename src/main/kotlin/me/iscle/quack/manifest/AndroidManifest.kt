package me.iscle.quack.manifest

data class AndroidManifest(
    val packageName: String,
    val versionCode: String,
    val versionName: String,
    val permissions: List<String>,
    val applicationSubclass: String?,
    val activities: List<ManifestActivity>,
    val services: List<ManifestService>,
    val receivers: List<ManifestReceiver>,
)
