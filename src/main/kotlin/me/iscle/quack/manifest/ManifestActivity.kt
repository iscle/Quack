package me.iscle.quack.manifest

data class ManifestActivity(
    val name: String,
    val permission: String?,
    val intentFilters: List<ManifestIntentFilter>,
)