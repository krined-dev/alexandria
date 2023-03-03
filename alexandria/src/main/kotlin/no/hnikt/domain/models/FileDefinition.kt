package no.hnikt.domain.models

/** * [FileDefinition] * Definition of the type of file and where to find it */
class FileDefinition(
    val path: String,
    val type: TypeDefinition,
    val fileType: TypeDefinition,
)