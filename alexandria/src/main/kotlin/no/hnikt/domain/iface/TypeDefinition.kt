package no.hnikt.domain.iface

import no.hnikt.domain.models.*

/** * [TypeDefinition] * Algebraic data type that we can use when defining a source definition */
sealed interface TypeDefinition{
    data class CodeDefinition(val type: CodeTypes, val description: String)
    data class DocumentDefinition(val description: String, val name: String)
}