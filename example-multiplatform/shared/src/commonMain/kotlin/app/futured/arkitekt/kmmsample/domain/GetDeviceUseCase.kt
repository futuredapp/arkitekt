package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.data.repository.DeviceRepository
import app.futured.arkitekt.kmmsample.data.repository.PersistenceRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph
import app.futured.arkitekt.kmusecases.freeze
import app.futured.arkitekt.kmusecases.usecase.UseCase

class GetDeviceUseCase : UseCase<Unit, Device>(), DeviceRepository by CommonGraph,
    PersistenceRepository by CommonGraph {
    init {
//        freeze() todo add to all UC and FC
    }
    override suspend fun build(arg: Unit): Device {
        val device = getDevice()
        dbManager.insertDevice(device)
        return device
    }
}
