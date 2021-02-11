package app.futured.arkitekt.kmmsample.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(val name: String, val os: String, val osVersion: String)
