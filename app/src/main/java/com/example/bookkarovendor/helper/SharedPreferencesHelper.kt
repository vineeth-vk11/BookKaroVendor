package com.example.bookkarovendor.helper

import android.content.Context

class SharedPreferencesHelper(val context: Context) {

    companion object {
        const val PREFS_NAME = "com.example.BookKaroVendor_Prefs"
        const val PREFS_FIELD_TYPE = "type"
        const val PREFS_KEY_TYPE_DELIVERY = 100L
        const val PREFS_KEY_TYPE_HOUSEHOLD = 101L
        const val PREFS_KEY_TYPE_SHOP = 102L
        const val PREFS_FIELD_SERVICES_PROVIDED = "servicesProvided"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()

    fun save(KEY_NAME: String, value: String) {
        editor.putString(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Long) {
        editor.putLong(KEY_NAME, value)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return prefs.getString(KEY_NAME, null)
    }

    fun getValueLong(KEY_NAME: String): Long {
        return prefs.getLong(KEY_NAME, 0)
    }

    fun clearSharedPreference() {
        editor.clear()
        editor.apply()
    }

}
