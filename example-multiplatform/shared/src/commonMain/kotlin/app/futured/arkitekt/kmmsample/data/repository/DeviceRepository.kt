package app.futured.arkitekt.kmmsample.data.repository

import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.di.module.NetworkModule

interface DeviceRepository : NetworkModule {
    suspend fun getDevice() : Device = apiManager.getDevice()
}
