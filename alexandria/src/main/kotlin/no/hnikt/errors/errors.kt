package no.hnikt.errors

sealed interface DomainError

sealed interface FileRepositoryError : DomainError {
    data class FileNotFound(val fileName: String) : FileRepositoryError
    data class InsertError(val error: String) : FileRepositoryError
}
