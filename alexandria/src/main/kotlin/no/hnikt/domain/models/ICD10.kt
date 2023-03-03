package no.hnikt.domain.models

import no.hnikt.repository.*

/** * [ICD10] ICD10 code */
data class ICD10(
    override val code: Code,
    override val description: String,
    override val version: Version) : AlexandriaCode