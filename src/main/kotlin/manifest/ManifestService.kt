package manifest

data class ManifestService(
    val name: String,
    val permission: String?,
    val intentFilters: List<ManifestIntentFilter>,
)
