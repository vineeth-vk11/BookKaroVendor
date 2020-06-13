package com.example.bookkarovendor.helper

import android.content.Context

class SharedPreferencesHelper(val context: Context) {

    companion object {
        const val PREFS_NAME = "com.example.BookKaroVendor_Prefs"
        const val PREFS_FIELD_TYPE = "type"
        const val PREFS_FIELD_CATEGORY = "category"

        //Service types:
        const val DELIVERY_SERVICE = 100L
        const val HOUSEHOLD_SERVICE = 101L
        const val SHOP_SERVICE = 102L

        //Service categories:
        const val DELIVERY_SERVICE_ORDER = 1000L
        const val DELIVERY_SERVICE_SEND_PACKAGES = 1001L
        const val HOUSEHOLD_SERVICE_PLUMBER = 1002L
        const val HOUSEHOLD_SERVICE_PEST_CONTROL = 1003L
        const val HOUSEHOLD_SERVICE_PAINTER = 1004L
        const val HOUSEHOLD_SERVICE_ELECTRICIAN = 1005L
        const val HOUSEHOLD_SERVICE_CARPENTER = 1006L
        const val SHOP_SERVICE_GENERAL_STORE = 1007L
        const val SHOP_SERVICE_BEAUTY_STORE = 1008L
        const val SHOP_SERVICE_CLOTHING_STORE = 1009L
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()

    fun save(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }

    fun getValueLong(key: String): Long {
        return prefs.getLong(key, 0L)
    }

    fun clearSharedPreference() {
        editor.clear()
        editor.apply()
    }

}
