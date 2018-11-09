package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, callback: (T?) -> Unit) {
    observe(lifecycleOwner, Observer { callback(it) })
}

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    observe(lifecycleOwner, Observer { it?.let(callback) })
}

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
    Transformations.map(this, func)

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>): LiveData<R> =
    Transformations.switchMap(this, func)
