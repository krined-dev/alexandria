package no.hnikt.utils

import arrow.core.continuations.Effect
import arrow.core.continuations.effect

import no.hnikt.domain.models.files.FileDefinition
import no.hnikt.domain.models.files.StoredFile
import no.hnikt.errors.DomainError

import java.io.File

context (FileDefinition)
fun fileReader() : Effect<DomainError, Map<String, StoredFile>> = effect {
    File(path).walkTopDown().filter {
        it.path.endsWith(type.name.lowercase()) }.map {
            val fileName = it.name
            fileName to StoredFile(fileName, "", version, it.readBytes())
        }.toMap()
}