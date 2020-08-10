package app.futured.arkitekt.crusecases.testusecases

import app.futured.arkitekt.crusecases.UseCase

class TestFailureUseCase : UseCase<Throwable, Unit>() {

    override suspend fun build(args: Throwable) {
        throw args
    }
}
