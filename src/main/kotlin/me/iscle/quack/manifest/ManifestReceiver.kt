package me.iscle.quack.manifest

data class ManifestReceiver(
    val name: String,
    val permission: String?,
    val intentFilters: List<ManifestIntentFilter>,
)