package app.futured.arkitekt.sample.domain

import com.thefuntasty.mvvm.rxusecases.usecases.MaybeUseCase
import io.reactivex.Maybe
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetStateUseCase @Inject constructor() : MaybeUseCase<Boolean, Boolean>() {

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
