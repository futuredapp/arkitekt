package app.futured.arkitekt.sample.domain.dummy

import app.futured.arkitekt.crusecases.UseCase
import app.futured.arkitekt.sample.tools.randomError
import kotlinx.coroutines.delay
import javax.inject.Inject

class SaveDataToFirstServerUseCase @Inject constructor() : UseCase<String, String>() {

    @Suppress("MagicNumber")
    override suspend fun build(args: String): String {
        delay(1000)
        randomError()
        return "Saved to first server"
    }
}
