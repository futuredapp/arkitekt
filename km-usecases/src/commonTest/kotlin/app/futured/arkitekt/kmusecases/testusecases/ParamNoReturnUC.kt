package app.futured.arkitekt.kmusecases.testusecases

import app.futured.arkitekt.kmusecases.usecase.UseCase
import app.futured.arkitekt.kmusecases.data.SomeParam
import kotlinx.coroutines.delay

class ParamNoReturnUC : UseCase<SomeParam, Unit>() {
    override suspend fun build(arg: SomeParam) {
        val name = arg.name
        delay(400)
    }
}
