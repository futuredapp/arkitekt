package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.usecases.base.BaseMayber
import io.reactivex.Maybe
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StateUseCase @Inject constructor() : BaseMayber<Boolean, Boolean>() {

    companion object {
        private const val DELAY_MS = 3000L
    }

    override fun prepare(emitSuccess: Boolean) = Maybe.create<Boolean> { emitter ->
        if (emitSuccess) {
            emitter.onSuccess(true)
        } else {
            emitter.onComplete()
        }
    }.delay(DELAY_MS, TimeUnit.MILLISECONDS)
}
