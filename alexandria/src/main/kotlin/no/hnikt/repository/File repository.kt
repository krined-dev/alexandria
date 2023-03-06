package no.hnikt.repository

import arrow.core.continuations.Effect
import arrow.core.continuations.EffectScope
import arrow.core.continuations.effect
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.single
import no.hnikt.db.handler.*
import no.hnikt.domain.models.files.StoredFile
import no.hnikt.errors.FileRepositoryError

interface FileRepository {
    fun storeFile(file: StoredFile): Effect<FileRepositoryError, StoredFile>

    fun selectFile(filename: String, registry: String): Effect<FileRepositoryError, StoredFile>

    fun deleteFile(fileName: String, registry: String) : Effect<FileRepositoryError, Long>

    fun updateFile(file: StoredFile): Effect<FileRepositoryError, StoredFile>
}

context(EffectScope<FileRepositoryError>, R2DBC)
fun fileRepository(conn: ConnectionFactory) = object : FileRepository {
    override fun storeFile(file: StoredFile) = effect {
        val inserted = conn.transaction<FileRepositoryError, Long> { execute(
                """
                insert into files (filename, version, bytes, registry) values (?, ?, ?)
                """.trimIndent(), file.filename, file.version, file.bytes, file.registry) {
                FileRepositoryError.InsertError("Insert failed with exception: ${it.stackTraceToString()}")
            }
        }.bind()
        ensure(inserted == 1L) { FileRepositoryError.InsertError("Inserted wrong amount of files: $inserted")}
        selectFile(file.filename, file.registry).bind()
    }

    override fun selectFile(filename: String, registry: String): Effect<FileRepositoryError, StoredFile> = effect {
        conn.transaction {
            select(storedFileMapper,
                """
                select * from files where filename = ? and registry = ? 
                """.trimIndent(), storedFileMapper, filename, registry
            ) {
                FileRepositoryError.FileSelectError(it.stackTraceToString())
            }
        }.bind().single()
    }
    override fun deleteFile(fileName: String, registry: String): Effect<FileRepositoryError, Long> =
        conn.transaction {
            execute("""delete from files where filename = ? and registry = ?""", fileName, registry) {
               FileRepositoryError.FileNotFound("File not found: $fileName")
            }
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

