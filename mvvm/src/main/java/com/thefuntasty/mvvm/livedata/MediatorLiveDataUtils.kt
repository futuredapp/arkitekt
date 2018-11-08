package com.thefuntasty.mvvm.livedata

import androidx.lifecycle.LiveData

fun <T1, T2, RESULT> combineLiveData(
        defaultValue: RESULT,
        t1: LiveData<T1>,
        t2: LiveData<T2>,
        callback: (T1, T2) -> RESULT
): DefaultValueMediatorLiveData<RESULT> = combineLiveData(t1, t2, callback)
        .nonNull(defaultValue)

fun <T1, T2, T3, RESULT> combineLiveData(
        defaultValue: RESULT,
        t1: LiveData<T1>,
        t2: LiveData<T2>,
        t3: LiveData<T3>,
        callback: (T1, T2, T3) -> RESULT
): DefaultValueMediatorLiveData<RESULT> = combineLiveData(t1, t2, t3, callback)
        .nonNull(defaultValue)

fun <T1, T2, T3, T4, RESULT> combineLiveData(
        defaultValue: RESULT,
        t1: LiveData<T1>,
        t2: LiveData<T2>,
        t3: LiveData<T3>,
        t4: LiveData<T4>,
        callback: (T1, T2, T3, T4) -> RESULT
): DefaultValueMediatorLiveData<RESULT> = combineLiveData(t1, t2, t3, t4, callback)
        .nonNull(defaultValue)

fun <T1, T2, T3, T4, T5, RESULT> combineLiveData(
        defaultValue: RESULT,
        t1: LiveData<T1>,
        t2: LiveData<T2>,
        t3: LiveData<T3>,
        t4: LiveData<T4>,
        t5: LiveData<T5>,
        callback: (T1, T2, T3, T4, T5) -> RESULT
): DefaultValueMediatorLiveData<RESULT> = combineLiveData(t1, t2, t3, t4, t5, callback)
        .nonNull(defaultValue)

fun <T1, T2, T3, T4, T5, T6, RESULT> combineLiveData(
        defaultValue: RESULT,
        t1: LiveData<T1>,
        t2: LiveData<T2>,
        t3: LiveData<T3>,
        t4: LiveData<T4>,
        t5: LiveData<T5>,
        t6: LiveData<T6>,
        callback: (T1, T2, T3, T4, T5, T6) -> RESULT
): DefaultValueMediatorLiveData<RESULT> = combineLiveData(t1, t2, t3, t4, t5, t6, callback)
        .nonNull(defaultValue)

fun <T1, T2, T3, T4, T5, T6, T7, RESULT> combineLiveData(
        defaultValue: RESULT,
        t1: LiveData<T1>,
        t2: LiveData<T2>,
        t3: LiveData<T3>,
        t4: LiveData<T4>,
        t5: LiveData<T5>,
        t6: LiveData<T6>,
        t7: LiveData<T7>,
        callback: (T1, T2, T3, T4, T5, T6, T7) -> RESULT
): DefaultValueMediatorLiveData<RESULT> = combineLiveData(t1, t2, t3, t4, t5, t6, t7, callback)
        .nonNull(defaultValue)
