package com.thefuntasty.mvvmsample.domain.dummy

import app.futured.arkitekt.crusecases.UseCase
import com.thefuntasty.mvvmsample.tools.randomError
import kotlinx.coroutines.delay
import javax.inject.Inject

class SaveDataToSecondServerUseCase @Inject constructor() : UseCase<String, String>() {

    @Suppress("MagicNumber")
    override suspend fun build(args: String): String {
        delay(1000)
        randomError()
        return "Saved to second server"
    }
}
