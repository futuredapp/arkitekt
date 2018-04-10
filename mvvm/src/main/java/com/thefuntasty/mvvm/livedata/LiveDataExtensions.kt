package com.thefuntasty.mvvm.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, callback: (T?) -> Unit) {
    observe(lifecycleOwner, Observer { callback(it) })
}
