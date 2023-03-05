package no.hnikt.domain.models.files

import no.hnikt.domain.models.TypeDefinition

/**
 * [FileDefinition] * Definition of the type of file and where to find it
 **/
class FileDefinition(
    val path: String,
    val type: TypeDefinition,
    val fileType: TypeDefinition,
)