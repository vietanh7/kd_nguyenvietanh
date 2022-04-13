package com.test.demo.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.SearchView

class StyledSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.searchViewStyle
) : SearchView(context, attrs, defStyleAttr) {

    init {
        val searchIcon = findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setColorFilter(context.getThemeColor(com.google.android.material.R.attr.colorOnPrimary))
    }
}