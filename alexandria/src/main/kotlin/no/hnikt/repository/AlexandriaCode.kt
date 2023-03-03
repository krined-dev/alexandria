package no.hnikt.repository

import no.hnikt.domain.models.*

/** * [AlexandriaCode] Defines the common interface for a * code. This is the base information a code needs to hold */
interface AlexandriaCode {
    val code: Code
    val description: String
    val version: Version
}