package app.futured.arkitekt.sample.domain

import app.futured.arkitekt.crusecases.FlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStateUseCase @Inject constructor() : FlowUseCase<Boolean, Boolean>() {

    companion object {
        private const val DELAY_MS = 3000L
    }

    override fun build(emitSuccess: Boolean): Flow<Boolean> = flow {
        delay(DELAY_MS)
        if (emitSuccess) {
            emit(true)
        }
    }
}
