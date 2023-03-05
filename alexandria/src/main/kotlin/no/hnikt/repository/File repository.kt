package no.hnikt.repository

import arrow.core.continuations.Effect
import arrow.core.continuations.EffectScope
import arrow.core.continuations.effect
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import no.hnikt.db.handler.R2DBC
import no.hnikt.db.handler.R2DBCMapper
import no.hnikt.db.handler.execute
import no.hnikt.db.handler.select
import no.hnikt.domain.models.files.StoredFile
import no.hnikt.errors.FileRepositoryError

interface FileRepository {
    fun storeFile(file: StoredFile): Effect<FileRepositoryError, StoredFile>

    fun selectFile(filename: String, registry: String): Effect<FileRepositoryError, StoredFile>

    fun deleteFile(fileName: String, registry: String) : Effect<FileRepositoryError, Long>

    fun updateFile(file: StoredFile): Effect<FileRepositoryError, StoredFile>
}

context(R2DBC)
fun fileRepository(conn: ConnectionFactory) = object : FileRepository {
    override fun storeFile(file: StoredFile) = effect {
        val inserted = conn.transaction(FileRepositoryError.InsertError("Error inserting file: ${file.filename}")) {
            execute(
                """
                insert into files (filename, version, bytes, registry) values (?, ?, ?)
                """.trimIndent(), file.filename, file.version, file.bytes, file.registry)
        }.bind()
        ensure(inserted == 1L) { FileRepositoryError.InsertError("Inserted wrong amount of files: $inserted")}
        selectFile(file.filename, file.registry).bind()
    }

    override fun selectFile(filename: String, registry: String): Effect<FileRepositoryError, StoredFile> = effect {
        conn.transaction(FileRepositoryError.FileNotFound(filename)) {
            select(
                """
               select * from files where filename = ? and registry = ? 
            """.trimIndent(), storedFileMapper, filename, registry
            )
        }.bind().single()
    }
    override fun deleteFile(fileName: String, registry: String): Effect<FileRepositoryError, Long> =
        conn.transaction(FileRepositoryError.FileNotFound(fileName)) {
            execute("""delete from files where filename = ? and registry = ?""", fileName, registry)
        }

    override fun updateFile(file: StoredFile): Effect<FileRepositoryError, StoredFile> = TODO()
}

private val storedFileMapper: R2DBCMapper<StoredFile> = { row, rowMetadata ->
    StoredFile(
        row.get("filename", String::class.java)!!,
        row.get("registry", String::class.java)!!,
        row.get("version", Int::class.java)!!,
        row.get("bytes", ByteArray::class.java)!!
    )
}

