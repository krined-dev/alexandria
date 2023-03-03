package no.hnikt.domain.models

import kotlinx.serialization.Serializable

/** * [ExternalHttpRequest] Placeholder for a request that is made to an external HTTP data source */
@Serializable
data class ExternalHttpRequest(
    val request: String,
)