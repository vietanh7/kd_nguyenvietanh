package com.test.demo.data

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ThrottleHelper @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREFS_FILE_NAME = "throttle-prefs"
    }

    private val prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    fun canRefresh(key: String, timeOut: Long): Boolean {
        val now = System.currentTimeMillis()
        val lastTime = prefs.getLong(key, -timeOut)
        if (now - lastTime > timeOut) {
            prefs.edit { putLong(key, now)}
            return true
        }

        return false
    }
}