package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseUseCase

class TestFailureUseCase : BaseUseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable) {
        throw args
    }
}
