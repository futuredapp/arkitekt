package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseCoroutiner
import kotlinx.coroutines.delay

class TestInteractor : BaseCoroutiner<Int, Int>() {

    override suspend fun build(args: Int): Int {
        println(Thread.currentThread().name)
        delay(1000)
        return args
    }
}
