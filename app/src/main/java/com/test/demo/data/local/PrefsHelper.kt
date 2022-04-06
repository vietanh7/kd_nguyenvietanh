package com.test.demo.data.local

import android.content.Context

class PrefsHelper(context: Context) {
    companion object {
        private const val PREF_FILE_NAME = "prefs_file"
    }

    private val sharePrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

}