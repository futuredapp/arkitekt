package com.thefuntasty.mvvm.crusecases.testusecases

import com.thefuntasty.mvvm.crusecases.UseCase

class TestFailureUseCase : UseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable) {
        throw args
    }
}
