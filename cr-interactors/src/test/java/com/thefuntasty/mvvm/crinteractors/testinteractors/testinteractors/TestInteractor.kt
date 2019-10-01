package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseCoroutineInteractor
import kotlinx.coroutines.delay
import kotlin.properties.Delegates

class TestInteractor : BaseCoroutineInteractor<Int>() {

    private var returnValue by Delegates.notNull<Int>()

    fun init(returnValue: Int) = apply {
        this.returnValue = returnValue
    }

    override suspend fun build(): Int {
        println(Thread.currentThread().name)
        delay(1000)
        return returnValue
    }
}
