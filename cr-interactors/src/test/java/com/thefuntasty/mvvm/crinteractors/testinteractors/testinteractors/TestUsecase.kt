package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseUsecase
import kotlinx.coroutines.delay

class TestUsecase : BaseUsecase<Int, Int>() {

    override suspend fun build(args: Int): Int {
        println(Thread.currentThread().name)
        delay(1000)
        return args
    }
}
