package app.futured.arkitekt.kmmsample.data.repository

import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.di.module.PersistenceModule
import kotlinx.coroutines.flow.Flow

interface PersistenceRepository : PersistenceModule {
    fun observeDevices(): Flow<List<Device>> = dbManager.getDevices()
//
    fun observeLaunches(): Flow<List<LaunchUi>> = dbManager.observeLaunches()

    suspend fun insertLaunches(list: List<LaunchUi>) = dbManager.insertLaunches(list)
}
