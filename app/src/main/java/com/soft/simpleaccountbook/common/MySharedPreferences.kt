package com.soft.simpleaccountbook.common

import android.content.Context
import android.content.SharedPreferences
import com.soft.simpleaccountbook.BuildConfig

class MySharedPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.prefs_name,Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String?): String {
        return prefs.getString(key, defValue).toString()
    }

    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun removeString(key: String) {
        prefs.edit().remove(key).apply()
    }
}