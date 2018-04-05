package com.annapol04.munchkin.util;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Transformations {
    @MainThread
    public static <X, Y> LiveData<Y> map(@NonNull LiveData<X> source,
                                         @NonNull final Function<X, Y> func) {
        return android.arch.lifecycle.Transformations.map(source, func);
    }

    @MainThread
    public static <X, Y> LiveData<Y> switchMap(@NonNull LiveData<X> trigger,
                                               @NonNull final Function<X, LiveData<Y>> func) {
        return android.arch.lifecycle.Transformations.switchMap(trigger, func);
    }

    @MainThread
    public static <X, Y> NonNullLiveData<Y> map(@NonNull Y initialValue,
                                                @NonNull NonNullLiveData<X> source,
                                                @NonNull final Function<X, Y> func) {
        final NonNullMediatorLiveData<Y> result = new NonNullMediatorLiveData<>(initialValue);
        result.addSource(source, x -> result.setValue(func.apply(x)));
        return result;
    }

    @MainThread
    public static <X, Y> NonNullLiveData<Y> switchMap(@NonNull Y initialValue,
                                                      @NonNull NonNullLiveData<X> trigger,
                                                      @NonNull final Function<X, NonNullLiveData<Y>> func) {
        final NonNullMediatorLiveData<Y> result = new NonNullMediatorLiveData<>(initialValue);
        result.addSource(trigger, new Observer<X>() {
            NonNullLiveData<Y> mSource;

            @Override
            public void onChanged(@Nullable X x) {
                NonNullLiveData<Y> newLiveData = func.apply(x);
                if (mSource == newLiveData) {
                    return;
                }
                if (mSource != null) {
                    result.removeSource(mSource);
                }
                mSource = newLiveData;
                if (mSource != null) {
                    result.addSource(mSource, result::setValue);
                }
            }
        });
        return result;
    }
}
