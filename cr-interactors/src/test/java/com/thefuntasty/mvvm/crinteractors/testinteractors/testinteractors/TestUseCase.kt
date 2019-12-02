package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseUseCase
import kotlinx.coroutines.delay

class TestUseCase : BaseUseCase<Int, Int>() {

    override suspend fun build(args: Int): Int {
        println(Thread.currentThread().name)
        delay(1000)
        return args
    }
}
