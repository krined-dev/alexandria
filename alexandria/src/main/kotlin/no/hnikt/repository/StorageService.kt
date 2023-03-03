package no.hnikt.repository

import no.hnikt.domain.models.*

/** * [StorageService] * Interface that describes the methods that we need for our storage service * that controls the storage and retrieval of codes from the repository */
interface StorageService {
    fun storeCodes(codes: List<AlexandriaCode>)
    fun retrieveCodes(codeType: CodeTypes, version: Version) : List<AlexandriaCode>
    fun updateCodes(codes: List<AlexandriaCode>)
}