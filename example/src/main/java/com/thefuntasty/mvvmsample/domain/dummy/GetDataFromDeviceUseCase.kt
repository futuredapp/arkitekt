package com.thefuntasty.mvvmsample.domain.dummy

import com.thefuntasty.mvvm.crusecases.UseCase
import com.thefuntasty.mvvmsample.tools.randomError
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetDataFromDeviceUseCase @Inject constructor() : UseCase<Unit, String>() {
    override suspend fun build(args: Unit): String {
        delay(1000)
        randomError()
        return "Data from device"
    }
}
