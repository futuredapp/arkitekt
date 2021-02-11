package app.futured.arkitekt.kmmsample

import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.model.MissionUi

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

class DatabaseManager {
    private val db = createDb()

    fun getDevices(): Flow<List<Device>> = db.devicesDbQueries
        .selectAll { id, name, os, sdk_version ->
            Device(name, os, sdk_version.toString())
        }
        .asFlow()
        .mapToList()

    fun observeLaunches(): Flow<List<LaunchUi>> = db.devicesDbQueries
        .selectAllLaunches { launchid, id, site, missionname ->
            LaunchUi(id, site, MissionUi(missionname))
        }
        .asFlow()
        .mapToList()

    suspend fun insertDevice(device: Device) =
        db.devicesDbQueries.insertItem(null, device.name, device.os)

    suspend fun insertLaunches(launches: List<LaunchUi>) = launches.forEach {
        db.devicesDbQueries.insertLaunchItem(it.id, it.site, it.mission.name)
    }
}
