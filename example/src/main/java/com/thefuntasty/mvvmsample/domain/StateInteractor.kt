package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.interactors.BaseMayber
import io.reactivex.Maybe
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StateInteractor @Inject constructor() : BaseMayber<Boolean>() {

    private var emitSuccess: Boolean = false

    companion object {
        private const val DELAY_MS = 3000L
    }

    fun init(emitSuccess: Boolean) = apply {
        this.emitSuccess = emitSuccess
    }

    override fun prepare(): Maybe<Boolean> = Maybe.create<Boolean> { emitter ->
        if (emitSuccess) {
            emitter.onSuccess(true)
        } else {
            emitter.onComplete()
        }
    }.delay(DELAY_MS, TimeUnit.MILLISECONDS)
}
