package app.futured.arkitekt.crusecases.testusecases

import app.futured.arkitekt.crusecases.UseCase
import kotlinx.coroutines.delay

class TestUseCase : UseCase<Int, Int>() {

    override suspend fun build(args: Int): Int {
        delay(1000)
        return args
    }
}
