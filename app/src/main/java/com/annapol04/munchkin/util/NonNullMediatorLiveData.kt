package com.annapol04.munchkin.util

import android.arch.lifecycle.Observer
import android.support.annotation.CallSuper
import android.support.annotation.MainThread


class NonNullMediatorLiveData<T>(initialValue: T) : NonNullMutableLiveData<T>(initialValue) {
    private val mSources = SafeIterableMap<NonNullLiveData<*>, Source<*>>()

    /**
     * Starts to listen the given `source` LiveData, `onChanged` observer will be called
     * when `source` value was changed.
     *
     *
     * `onChanged` callback will be called only when this `MediatorLiveData` is active.
     *
     *  If the given LiveData is already added as a source but with a different Observer,
     * [IllegalArgumentException] will be thrown.
     *
     * @param source    the `LiveData` to listen to
     * @param onChanged The observer that will receive the events
     * @param <S>       The type of data hold by `source` LiveData
    </S> */
    @MainThread
    fun <S> addSource(source: NonNullLiveData<S>, onChanged: Observer<S>) {
        val e = Source(source, onChanged)
        val existing = mSources.putIfAbsent(source, e)
        if (existing != null && existing.mObserver !== onChanged) {
            throw IllegalArgumentException(
                    "This source was already added with the different observer")
        }
        if (existing != null) {
            return
        }
        if (hasActiveObservers()) {
            e.plug()
        }
    }

    /**
     * Stops to listen the given `LiveData`.
     *
     * @param toRemote `LiveData` to stop to listen
     * @param <S>      the type of data hold by `source` LiveData
    </S> */
    @MainThread
    fun <S> removeSource(toRemote: NonNullLiveData<S>) {
        val source = mSources.remove(toRemote)
        source?.unplug()
    }

    @CallSuper
    override fun onActive() {
        for ((_, value) in mSources) {
            value.plug()
        }
    }

    @CallSuper
    override fun onInactive() {
        for ((_, value) in mSources) {
            value.unplug()
        }
    }

    private class Source<V> internal constructor(internal val mLiveData: NonNullLiveData<V>, internal val mObserver: Observer<V>) : Observer<V> {
        internal var mVersion = -1

        internal fun plug() {
            mLiveData.observeForever(this)
        }

        internal fun unplug() {
            mLiveData.removeObserver(this)
        }

        override fun onChanged(v: V?) {
            if (mVersion != mLiveData.version) {
                mVersion = mLiveData.version
                mObserver.onChanged(v)
            }
        }
    }
}
