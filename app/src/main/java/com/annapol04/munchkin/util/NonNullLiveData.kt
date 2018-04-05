package com.annapol04.munchkin.util

import android.arch.lifecycle.LiveData

/**
 * A LiveData implementation with an initial value and that does not allow null values.
 */
open class NonNullLiveData<T>(initialValue: T) : LiveData<T>() {
    init {
        value = initialValue
    }

    var version = -1

    /**
     * Returns the current (non-null) value. Note that calling this method on a background thread
     * does not guarantee that the latest value set will be received.
     */
    override fun getValue(): T = super.getValue() ?: throw IllegalStateException("Value is null")

    /**
     * Posts a task to a main thread to set the given (non null) value.
     */
    override fun postValue(value: T?) {
        if (value == null) {
            throw IllegalArgumentException("Value cannot be null")
        }
        super.postValue(value)
    }

    /**
     * Sets the (non-null) value. If there are active observers, the value will be dispatched to them.
     */
    override fun setValue(value: T?) {
        if (value == null) {
            throw IllegalArgumentException("Value cannot be null")
        }
        version++
        super.setValue(value)
    }
}