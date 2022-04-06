package com.test.demo.data.local

import android.content.Context
import androidx.core.content.edit

class PrefsHelper(context: Context) {
    companion object {
        private const val PREF_FILE_NAME = "prefs_file"
        private const val TOKEN_KEY = "token_key"
    }

    private val sharePrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)


    fun saveToken(token: String) {
        sharePrefs.edit { putString(TOKEN_KEY, token) }
    }

    fun getToken(): String? {
        return sharePrefs.getString(TOKEN_KEY, "")
    }
}