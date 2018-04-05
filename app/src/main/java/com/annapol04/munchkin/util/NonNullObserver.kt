package com.annapol04.munchkin.util

import android.arch.lifecycle.Observer

abstract class NonNullObserver<T> : Observer<T> {
    protected abstract fun onValueChanged(t: T)

    override fun onChanged(value: T?) {
        value?.let {
            onValueChanged(it)
        }
    }
}