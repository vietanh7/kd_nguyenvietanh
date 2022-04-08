package com.test.demo.features.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.test.demo.R

abstract class BaseActivity<B: ViewBinding>: AppCompatActivity() {
    abstract val binding: B

    @IdRes
    open val navHostId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    open fun showMessage(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Message")
            .setMessage(message)
            .setNeutralButton("Ok") { _, _ -> }
            .show()
    }
}