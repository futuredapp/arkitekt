package app.futured.arkitekt.kmmsample.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QueryData(val data: DataElement)

@Serializable
data class DataElement(val launches: LaunchesElement)

@Serializable
data class LaunchesElement(val launches: List<LaunchUi>)
