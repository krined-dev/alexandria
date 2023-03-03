package no.hnikt.repository

import no.hnikt.domain.models.*

/** * The definition of a data source */
interface Source {
    val url: String
    val name: String
    val code: CodeTypes
}