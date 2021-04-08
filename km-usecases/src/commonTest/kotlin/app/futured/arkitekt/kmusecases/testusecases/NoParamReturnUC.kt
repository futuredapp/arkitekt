package app.futured.arkitekt.kmusecases.testusecases

import app.futured.arkitekt.kmusecases.usecase.UseCase
import app.futured.arkitekt.kmusecases.data.SomeReturn
import kotlinx.coroutines.delay

class NoParamReturnUC : UseCase<Unit, SomeReturn>() {
    override suspend fun build(arg: Unit): SomeReturn {
        delay(400)
        return SomeReturn(1, "test", true)
    }
}
