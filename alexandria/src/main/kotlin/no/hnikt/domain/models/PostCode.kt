package no.hnikt.domain.models

import kotlinx.serialization.Serializable

/** * [PostCode] Norwegian Postal code. The number is in the range 0000-9999 */
@Serializable
data class PostCode (
    val number: String,
    val country: String,
    val municipalty: String,
    val city: String,
)