package manifest

data class ManifestReceiver(
    val name: String,
    val permission: String?,
    val intentFilters: List<ManifestIntentFilter>,
)