package com.example.alchoholorgas

import android.content.Context

class SharedPreferencesManager (context: Context) {

    private val sharedPreferences = context.getSharedPreferences(context.resources.getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

    fun saveString (key: String, value: String) {
        val editor = sharedPreferences.edit()
        with(editor) {
            putString(key, value)
            apply()
        }
    }

    fun loadString (key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveBoolean (key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        with(editor) {
            putBoolean(key, value)
            apply()
        }
    }

    fun loadBoolean (key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue) ?: defaultValue
    }
}