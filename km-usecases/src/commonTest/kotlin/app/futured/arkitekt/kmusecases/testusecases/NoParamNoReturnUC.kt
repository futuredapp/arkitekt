package app.futured.arkitekt.kmusecases.testusecases

import app.futured.arkitekt.kmusecases.usecase.UseCase
import kotlinx.coroutines.delay

class NoParamNoReturnUC : UseCase<Unit, Unit>() {
    override suspend fun build(arg: Unit) {
        delay(400)
    }
}
