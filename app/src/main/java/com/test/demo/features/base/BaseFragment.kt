package com.test.demo.features.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B: ViewBinding>(layoutRes: Int): Fragment(layoutRes) {
    abstract val binding: B
}