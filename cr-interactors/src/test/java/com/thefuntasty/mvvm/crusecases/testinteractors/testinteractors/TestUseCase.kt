package com.thefuntasty.mvvm.crusecases.testinteractors.testinteractors

import com.thefuntasty.mvvm.crusecases.BaseUseCase
import kotlinx.coroutines.delay

class TestUseCase : BaseUseCase<Int, Int>() {

    override suspend fun build(args: Int): Int {
        println(Thread.currentThread().name)
        delay(1000)
        return args
    }
}
