package app.futured.arkitekt.kmmsample.domain

import app.futured.arkitekt.kmusecases.usecase.FlowUseCase
import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.data.repository.PersistenceRepository
import app.futured.arkitekt.kmmsample.di.CommonGraph

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFromPersistenceUseCase : FlowUseCase<Unit, DevicesWrapper>(),
    PersistenceRepository by CommonGraph {
    override fun build(arg: Unit): Flow<DevicesWrapper> = observeDevices()
        .map {
            DevicesWrapper(it)
        }
}

data class DevicesWrapper(val list: List<Device>)
