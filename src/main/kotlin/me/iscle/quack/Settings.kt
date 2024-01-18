package me.iscle.quack

import java.util.prefs.Preferences
import kotlin.reflect.KProperty

object Settings {
    private val preferences = Preferences.userNodeForPackage(Settings::class.java)

    var selectedLocale by StringPreferenceDelegate("selectedLocale", "en_US")
    var showExitDialog by BooleanPreferenceDelegate("showExitDialog", true)

    fun init(args: QuackArgs) {

    }

    private class BooleanPreferenceDelegate(
        private val key: String,
        private val defaultValue: Boolean,
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
            return preferences.getBoolean(key, defaultValue)
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            preferences.putBoolean(key, value)
        }
    }

    private class StringPreferenceDelegate(
        private val key: String,
        private val defaultValue: String,
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return preferences.get(key, defaultValue)
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            preferences.put(key, value)
        }
    }
}