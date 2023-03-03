package no.hnikt.domain.models

import no.hnikt.repository.*

/** * [NCMP] NCMP Code */
data class NCMP(
    override val code: Code,
    override val description: String,
    override val version: Version) : AlexandriaCode