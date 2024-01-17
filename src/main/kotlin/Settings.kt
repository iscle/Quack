object Settings {
    private var initialized = false

    fun init() {
        initialized = true
    }

    fun getSelectedLocale(): String {
        return "en_US"
    }
}