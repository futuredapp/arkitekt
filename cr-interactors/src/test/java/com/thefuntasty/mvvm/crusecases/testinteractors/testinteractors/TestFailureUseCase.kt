package com.thefuntasty.mvvm.crusecases.testinteractors.testinteractors

import com.thefuntasty.mvvm.crusecases.BaseUseCase

class TestFailureUseCase : BaseUseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable) {
        throw args
    }
}
