package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseUsecase

class TestFailureUsecase : BaseUsecase<Throwable, Unit>() {

    override suspend fun build(args: Throwable) {
        throw args
    }
}
