package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmusecases.freeze
import app.futured.arkitekt.kmusecases.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveDeviceUseCase : FlowUseCase<Unit, ListWrapper<Device>>() {
    init {
        freeze()
    }
    override fun build(arg: Unit): Flow<ListWrapper<Device>> = flow {

        var list = listOf(
            Device(
                "Pixel",
                "Android",
                "11",
            )
        )
        while (true) {
            emit(ListWrapper(list))
            kotlinx.coroutines.delay(2_000)
            list += Device(
                "Pixel ${list.size}",
                "Android",
                "11",
            )
        }
    }
}
