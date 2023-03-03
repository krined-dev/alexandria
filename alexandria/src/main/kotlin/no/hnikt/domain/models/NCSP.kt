package no.hnikt.domain.models

import no.hnikt.repository.*

/** * [NCSP] NCSP Code */
data class NCSP(
    override val code: Code,
    override val description: String,
    override val version: Version) : AlexandriaCode