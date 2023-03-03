package no.hnikt.domain.models

import kotlinx.serialization.Serializable

/** *  [ExternalHttpResponse] Placeholder for a request that is received from an external HTTP data source */
@Serializable
data class ExternalHttpResponse(
    val response: String,
)