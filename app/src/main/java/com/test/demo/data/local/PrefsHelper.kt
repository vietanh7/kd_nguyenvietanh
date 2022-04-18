package com.test.demo.data.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface PrefsHelper {
    fun saveToken(token: String)

    fun getToken(): String?
}

class PrefsHelperImpl @Inject constructor(@ApplicationContext context: Context): PrefsHelper {
    companion object {
        private const val PREF_FILE_NAME = "prefs_file"
        private const val TOKEN_KEY = "token_key"
    }

    private val sharePrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)


    override fun saveToken(token: String) {
        sharePrefs.edit { putString(TOKEN_KEY, token) }
    }

    override fun getToken(): String? {
        return sharePrefs.getString(TOKEN_KEY, "")
    }
}