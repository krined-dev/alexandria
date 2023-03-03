package no.hnikt.domain.models

import AlexandriaCode

/** * [CodeResponse] * HTTP response with a set of codes to be delivered to a registry */
data class CodeResponse(
    val codes: List<AlexandriaCode>
)