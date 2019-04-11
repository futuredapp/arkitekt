package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, callback: (T?) -> Unit) {
    observe(lifecycleOwner, Observer { callback(it) })
}

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    observe(lifecycleOwner, Observer { it?.let(callback) })
}

/**
 * @Deprecated this method was deprecated in favour of androidx.lifecycle:lifecycle-livedata-ktx implementation
 * @see <a href="https://dl.google.com/dl/android/maven2/index.html">google maven repo</a> for latest release
 */
@Deprecated(message = "use androidx.lifecycle.map from androidx.lifecycle:lifecycle-livedata-ktx")
fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> = this.map(func)

/**
 * @Deprecated this method was deprecated in favour of androidx.lifecycle:lifecycle-livedata-ktx implementation
 * @see <a href="https://dl.google.com/dl/android/maven2/index.html">google maven repo</a> for latest release
 */
@Deprecated(message = "use androidx.lifecycle.switchMap from androidx.lifecycle:lifecycle-livedata-ktx")
fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>): LiveData<R> = this.switchMap(func)

/**
 * @Deprecated this method was deprecated in favour of androidx.lifecycle:lifecycle-livedata-ktx implementation
 * @see <a href="https://dl.google.com/dl/android/maven2/index.html">google maven repo</a> for latest release
 */
@Deprecated(message = "use androidx.lifecycle.distinctUntilChanged from androidx.lifecycle:lifecycle-livedata-ktx")
fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = this.distinctUntilChanged()
