package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.data.repository.DeviceRepository
import app.futured.arkitekt.kmmsample.data.repository.PersistenceRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph
import app.futured.arkitekt.kmusecases.UseCase

class GetDeviceUseCase : UseCase<Unit, Device>(), DeviceRepository by CommonGraph,
    PersistenceRepository by CommonGraph {
    override suspend fun build(arg: Unit): Device {
        val device = getDevice()
        dbManager.insertDevice(device)
        return device
    }
}
