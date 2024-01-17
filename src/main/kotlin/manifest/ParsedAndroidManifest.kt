package manifest

import manifest.ManifestActivity
import manifest.ManifestReceiver
import manifest.ManifestService

data class ParsedAndroidManifest(
    val packageName: String,
    val versionCode: String,
    val versionName: String,
    val permissions: List<String>,
    val applicationSubclass: String?,
    val activities: List<ManifestActivity>,
    val services: List<ManifestService>,
    val receivers: List<ManifestReceiver>,
)
