package app.futured.arkitekt.kmmsample.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LaunchUi(val id: String, val site: String, val mission: MissionUi)

@Serializable
data class MissionUi(val name: String)
