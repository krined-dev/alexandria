package no.hnikt.service

import no.hnikt.domain.models.files.FileDefinition

/** * [FileService] * Interface that describes methods for reading files. Takes a [FileDefinition] * that defines the type of file and where to find it. */
interface FileService {
    fun readFiles(definition: FileDefinition)
}