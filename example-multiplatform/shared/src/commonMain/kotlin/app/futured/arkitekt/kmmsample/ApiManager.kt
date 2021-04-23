package app.futured.arkitekt.kmmsample

import com.apollographql.apollo.api.toJson
import app.futured.arkitekt.kmmsample.data.model.Device
import app.futured.arkitekt.kmmsample.data.model.LaunchUi
import app.futured.arkitekt.kmmsample.data.model.QueryData
import app.futured.arkitekt.kmusecases.freeze
import com.rudolfhladik.arkitektexample.shared.GetLaunchesQuery
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.toByteString

class ApiManager {

    private val httpClient = HttpClient()

    private val graphQlurl = "https://apollo-fullstack-tutorial.herokuapp.com/graphql"

    private val json = Json(Json) {
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend fun getLaunches(): List<GetLaunchesQuery.Launch> {
        val query = GetLaunchesQuery()
        val request = query.composeRequestBody()
            .toByteArray()
        val response = httpClient.request<HttpResponse> {
            body = request
            method = HttpMethod.Post
            url(graphQlurl)
            header("Accept", "application/json")
            header("Content-Type", "application/json")
        }
        val responseData = query.parse(response.readBytes().toByteString())
        // manual parse
        val jsonString = responseData.data?.toJson() ?: error("Failed to parse")
        val list: List<LaunchUi> =
            json.decodeFromString(QueryData.serializer(), jsonString).data.launches.launches

        return responseData
            .data?.launches?.launches
            ?.mapNotNull { it }
            .orEmpty()
    }
}
