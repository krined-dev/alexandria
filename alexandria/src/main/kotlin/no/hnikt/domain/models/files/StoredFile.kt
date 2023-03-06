package no.hnikt.domain.models.files

import java.util.Base64

/**
 * [StoredFile] We store the file as a ByteArray
 */
data class StoredFile(
    val filename: String,
    val registry: String,
    val version: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoredFile

        if (version != other.version) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}

data class StoredFileBase64(
    val registry: String,
    val filename: String,
    val version: Int,
    val data: Base64String
)

@JvmInline
value class Base64String private constructor(val value: String) {
    companion object {
        fun fromBytes(bytes: ByteArray): String =
            Base64.getEncoder().encodeToString(bytes)
    }
}