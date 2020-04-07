package com.thefuntasty.mvvmsample.domain.dummy

import app.futured.arkitekt.crusecases.UseCase
import com.thefuntasty.mvvmsample.tools.randomError
import kotlinx.coroutines.delay
import javax.inject.Inject

class ConfirmDataSavedSuccessfullyUseCase @Inject constructor() : UseCase<Pair<String, String>, String>() {

    @Suppress("MagicNumber")
    override suspend fun build(args: Pair<String, String>): String {
        delay(1000)
        randomError()
        return "DONE"
    }
}
