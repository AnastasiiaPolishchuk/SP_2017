package com.annapol04.munchkin.util

import android.support.annotation.NonNull

open class NonNullMutableLiveData<T>(initialValue: T) : NonNullLiveData<T>(initialValue) {
    /**
     * Posts a task to a main thread to set the given (non-null) value.
     */
    public override fun postValue(value: T?) {
        super.postValue(value)
    }

    /**
     * Sets the (non-null) value. If there are active observers, the value will be dispatched to them.
     */
    public override fun setValue(value: T?) {
        super.setValue(value)
    }

    @NonNull
    override fun getValue(): T = super.getValue()
}