package no.hnikt.httpclient.external

import ExternalHttpRequest
import ExternalHttpResponse
import arrow.core.continuations.Effect
import arrow.core.continuations.effect
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import no.hnikt.domain.models.ClientConfiguration
import no.hnikt.errors.DomainError

fun <T>getInformation(
    request: ExternalHttpRequest,
    client: HttpClient,
    clientConfig: ClientConfiguration
) : Effect<DomainError, ExternalHttpResponse<T>> = effect {
    client.get(clientConfig.url) {
        setBody(request)
        headers {
            clientConfig.headers?.let { headers ->
                headers.forEach {
                    append(it.key, it.value)
                }
            }
        }
    }.body()
}

fun foo() {
    val header = mapOf("Content-Type" to "application/json")

}

@Serializable
data class PostNrResponse(val postnummer: Int, val county: String)