package app.futured.arkitekt.kmusecases.testusecases

import app.futured.arkitekt.kmusecases.UseCase
import app.futured.arkitekt.kmusecases.data.SomeParam
import app.futured.arkitekt.kmusecases.data.SomeReturn
import kotlinx.coroutines.delay

class ParamReturnUC : UseCase<SomeParam, SomeReturn>() {
    override suspend fun build(arg: SomeParam): SomeReturn {
        delay(400)
        return SomeReturn(1, "test", true)
    }
}
