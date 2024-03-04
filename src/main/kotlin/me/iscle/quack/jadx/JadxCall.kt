package me.iscle.quack.jadx

class JadxCall (
    var method: JadxMethod? = null,
    var arguments: List<Argument> = emptyList(),
) {
    data class Argument(
        val type: String,
        val value: Any,
    )
}