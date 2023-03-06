package no.hnikt.domain.models

/** * ClientConfiguration */
data class ClientConfiguration(
    val url: String,
    val headers: Map<String, String>?
)