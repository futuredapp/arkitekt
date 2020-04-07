package app.futured.arkitekt.core.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations

fun <T1, T2, RESULT> combineLiveData(
    t1: LiveData<T1>,
    t2: LiveData<T2>,
    callback: (T1, T2) -> RESULT
): LiveData<RESULT> {
    val mediatorLiveData = MediatorLiveData<Pair<T1, T2>>().apply {
        var lastT1: T1? = null
        var lastT2: T2? = null

        fun update() {
            val localLastT1 = lastT1
            val localLastT2 = lastT2
            if (localLastT1 != null && localLastT2 != null) {
                this.value = Pair(localLastT1, localLastT2)
            }
        }

        addSource(t1) { lastT1 = it; update() }
        addSource(t2) { lastT2 = it; update() }
    }
    return Transformations.map(mediatorLiveData) { callback(it.first, it.second) }
}

fun <T1, T2, T3, RESULT> combineLiveData(
    t1: LiveData<T1>,
    t2: LiveData<T2>,
    t3: LiveData<T3>,
    callback: (T1, T2, T3) -> RESULT
): LiveData<RESULT> {

    val mediatorLiveData = MediatorLiveData<Triple<T1, T2, T3>>().apply {
        var lastT1: T1? = null
        var lastT2: T2? = null
        var lastT3: T3? = null

        fun update() {
            val localLastT1 = lastT1
            val localLastT2 = lastT2
            val localLastT3 = lastT3
            if (localLastT1 != null && localLastT2 != null && localLastT3 != null) {
                this.value = Triple(localLastT1, localLastT2, localLastT3)
            }
        }

        addSource(t1) { lastT1 = it; update() }
        addSource(t2) { lastT2 = it; update() }
        addSource(t3) { lastT3 = it; update() }
    }
    return Transformations.map(mediatorLiveData) { callback(it.first, it.second, it.third) }
}

@Suppress("LongParameterList", "ComplexCondition")
fun <T1, T2, T3, T4, RESULT> combineLiveData(
    t1: LiveData<T1>,
    t2: LiveData<T2>,
    t3: LiveData<T3>,
    t4: LiveData<T4>,
    callback: (T1, T2, T3, T4) -> RESULT
): LiveData<RESULT> {

    data class FourFold(val first: T1, val second: T2, val third: T3, val fourth: T4)

    val mediatorLiveData = MediatorLiveData<FourFold>().apply {
        var lastT1: T1? = null
        var lastT2: T2? = null
        var lastT3: T3? = null
        var lastT4: T4? = null

        fun update() {
            val localLastT1 = lastT1
            val localLastT2 = lastT2
            val localLastT3 = lastT3
            val localLastT4 = lastT4

            if (localLastT1 != null && localLastT2 != null && localLastT3 != null && localLastT4 != null) {
                this.value = FourFold(localLastT1, localLastT2, localLastT3, localLastT4)
            }
        }

        addSource(t1) { lastT1 = it; update() }
        addSource(t2) { lastT2 = it; update() }
        addSource(t3) { lastT3 = it; update() }
        addSource(t4) { lastT4 = it; update() }
    }
    return Transformations.map(mediatorLiveData) { callback(it.first, it.second, it.third, it.fourth) }
}

@Suppress("LongParameterList", "ComplexCondition")
fun <T1, T2, T3, T4, T5, RESULT> combineLiveData(
    t1: LiveData<T1>,
    t2: LiveData<T2>,
    t3: LiveData<T3>,
    t4: LiveData<T4>,
    t5: LiveData<T5>,
    callback: (T1, T2, T3, T4, T5) -> RESULT
): LiveData<RESULT> {

    data class FiveFold(val first: T1, val second: T2, val third: T3, val fourth: T4, val fifth: T5)

    val mediatorLiveData = MediatorLiveData<FiveFold>().apply {
        var lastT1: T1? = null
        var lastT2: T2? = null
        var lastT3: T3? = null
        var lastT4: T4? = null
        var lastT5: T5? = null

        fun update() {
            val localLastT1 = lastT1
            val localLastT2 = lastT2
            val localLastT3 = lastT3
            val localLastT4 = lastT4
            val localLastT5 = lastT5
            if (localLastT1 != null && localLastT2 != null && localLastT3 != null && localLastT4 != null && localLastT5 != null) {
                this.value = FiveFold(localLastT1, localLastT2, localLastT3, localLastT4, localLastT5)
            }
        }

        addSource(t1) { lastT1 = it; update() }
        addSource(t2) { lastT2 = it; update() }
        addSource(t3) { lastT3 = it; update() }
        addSource(t4) { lastT4 = it; update() }
        addSource(t5) { lastT5 = it; update() }
    }
    return Transformations.map(mediatorLiveData) { callback(it.first, it.second, it.third, it.fourth, it.fifth) }
}

@Suppress("LongParameterList", "ComplexCondition")
fun <T1, T2, T3, T4, T5, T6, RESULT> combineLiveData(
    t1: LiveData<T1>,
    t2: LiveData<T2>,
    t3: LiveData<T3>,
    t4: LiveData<T4>,
    t5: LiveData<T5>,
    t6: LiveData<T6>,
    callback: (T1, T2, T3, T4, T5, T6) -> RESULT
): LiveData<RESULT> {

    data class SixFold(val first: T1, val second: T2, val third: T3, val fourth: T4, val fifth: T5, val sixth: T6)

    val mediatorLiveData = MediatorLiveData<SixFold>().apply {
        var lastT1: T1? = null
        var lastT2: T2? = null
        var lastT3: T3? = null
        var lastT4: T4? = null
        var lastT5: T5? = null
        var lastT6: T6? = null

        fun update() {
            val localLastT1 = lastT1
            val localLastT2 = lastT2
            val localLastT3 = lastT3
            val localLastT4 = lastT4
            val localLastT5 = lastT5
            val localLastT6 = lastT6
            if (localLastT1 != null && localLastT2 != null && localLastT3 != null &&
                localLastT4 != null && localLastT5 != null && localLastT6 != null
            ) {
                this.value = SixFold(localLastT1, localLastT2, localLastT3, localLastT4, localLastT5, localLastT6)
            }
        }

        addSource(t1) { lastT1 = it; update() }
        addSource(t2) { lastT2 = it; update() }
        addSource(t3) { lastT3 = it; update() }
        addSource(t4) { lastT4 = it; update() }
        addSource(t5) { lastT5 = it; update() }
        addSource(t6) { lastT6 = it; update() }
    }
    return Transformations.map(mediatorLiveData) {
        callback(
            it.first,
            it.second,
            it.third,
            it.fourth,
            it.fifth,
            it.sixth
        )
    }
}

@Suppress("LongParameterList", "ComplexCondition")
fun <T1, T2, T3, T4, T5, T6, T7, RESULT> combineLiveData(
    t1: LiveData<T1>,
    t2: LiveData<T2>,
    t3: LiveData<T3>,
    t4: LiveData<T4>,
    t5: LiveData<T5>,
    t6: LiveData<T6>,
    t7: LiveData<T7>,
    callback: (T1, T2, T3, T4, T5, T6, T7) -> RESULT
): LiveData<RESULT> {

    data class SevenFold(
        val first: T1,
        val second: T2,
        val third: T3,
        val fourth: T4,
        val fifth: T5,
        val sixth: T6,
        val seventh: T7
    )

    val mediatorLiveData = MediatorLiveData<SevenFold>().apply {
        var lastT1: T1? = null
        var lastT2: T2? = null
        var lastT3: T3? = null
        var lastT4: T4? = null
        var lastT5: T5? = null
        var lastT6: T6? = null
        var lastT7: T7? = null

        fun update() {
            val localLastT1 = lastT1
            val localLastT2 = lastT2
            val localLastT3 = lastT3
            val localLastT4 = lastT4
            val localLastT5 = lastT5
            val localLastT6 = lastT6
            val localLastT7 = lastT7
            if (localLastT1 != null && localLastT2 != null && localLastT3 != null &&
                localLastT4 != null && localLastT5 != null && localLastT6 != null && localLastT7 != null
            ) {
                this.value =
                    SevenFold(localLastT1, localLastT2, localLastT3, localLastT4, localLastT5, localLastT6, localLastT7)
            }
        }

        addSource(t1) { lastT1 = it; update() }
        addSource(t2) { lastT2 = it; update() }
        addSource(t3) { lastT3 = it; update() }
        addSource(t4) { lastT4 = it; update() }
        addSource(t5) { lastT5 = it; update() }
        addSource(t6) { lastT6 = it; update() }
        addSource(t7) { lastT7 = it; update() }
    }
    return Transformations.map(mediatorLiveData) {
        callback(
            it.first,
            it.second,
            it.third,
            it.fourth,
            it.fifth,
            it.sixth,
            it.seventh
        )
    }
}
