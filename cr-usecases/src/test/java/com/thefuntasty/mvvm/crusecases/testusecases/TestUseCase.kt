package com.thefuntasty.mvvm.crusecases.testusecases

import com.thefuntasty.mvvm.crusecases.UseCase
import kotlinx.coroutines.delay

class TestUseCase : UseCase<Int, Int>() {

    override suspend fun build(args: Int): Int {
        println(Thread.currentThread().name)
        delay(1000)
        return args
    }
}
