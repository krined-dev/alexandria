package no.hnikt.repository

import no.hnikt.domain.models.*

/** * [FileService] * Interface that describes methods for reading files. Takes a [FileDefinition] * that defines the type of file and where to find it. */
interface FileService {
    fun readFiles(definition: FileDefinition)
}