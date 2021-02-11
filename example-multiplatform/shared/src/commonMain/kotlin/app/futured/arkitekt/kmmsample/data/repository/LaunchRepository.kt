package app.futured.arkitekt.kmmsample.data.repository

import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.model.MissionUi
import app.futured.arkitekt.kmmsample.di.module.NetworkModule

interface LaunchRepository : NetworkModule {
    suspend fun getLaunches(): List<LaunchUi> = apiManager.getLaunches()
        .map {
            LaunchUi(it.id, it.site ?: "", MissionUi(it.mission?.name?: "") )
        }
}
