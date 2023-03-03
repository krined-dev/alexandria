package no.hnikt.domain.models

/** * [CodeRequest] A HTTP request from a registry for a given set of codes */
data class CodeRequest(
    val codeType: CodeTypes,
    val version: Version,
    val fromRegistry: RegistryName
)