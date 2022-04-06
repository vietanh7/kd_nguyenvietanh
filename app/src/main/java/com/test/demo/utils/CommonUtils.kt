package com.test.demo.utils

import android.widget.EditText

fun EditText.setTextIfChanged(content: String) {
    if (content != text.toString()) {
        setTextKeepState(content)
    }
}