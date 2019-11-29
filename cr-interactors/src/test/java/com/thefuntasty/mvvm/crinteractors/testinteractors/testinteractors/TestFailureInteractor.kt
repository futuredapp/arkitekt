package com.thefuntasty.mvvm.crinteractors.testinteractors.testinteractors

import com.thefuntasty.mvvm.crinteractors.BaseCoroutiner

class TestFailureInteractor : BaseCoroutiner<Throwable, Unit>() {

    override suspend fun build(args: Throwable) {
        throw args
    }
}
