package no.hnikt.db.handler

import arrow.core.continuations.Effect
import arrow.core.continuations.EffectScope
import arrow.core.continuations.effect
import arrow.core.nonFatalOrThrow
import arrow.core.valid
import io.r2dbc.spi.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst

/**
 * Simple wrapper around r2dbc that gives us a kotlinx flow to handle connection
 * Inspired by https://gist.github.com/ferrerojosh/109f8931223c92ecc24f5a751759e793
 * Using arrow-fx for wrapping io in effect
 */

typealias R2DBCMapper<T> = (row: Row, rowMetadata: RowMetadata) -> T

sealed interface R2DBCError {
    object Error: R2DBCError
    data class TransactionError(val stacktrace: String) : R2DBCError
}

object R2DBC {
    inline fun <E, T>ConnectionFactory.transaction(
        crossinline handler: ConnectionHandle<E>.() -> Effect<E, T>): Effect<E, T> = effect {
        val connection = create().awaitFirst()
        try {
            val value = handler(ConnectionHandle(connection, this))
            connection.commitTransaction()
            value.fold({
                connection.rollbackTransaction()
                shift(it)
            }, { it })
        } catch (e: Exception) {
            connection.rollbackTransaction()
            throw e
        }
    }

}

class ConnectionHandle<E>(connection: Connection, raise: EffectScope<E>): Connection by connection, EffectScope<E> by raise {
    /**
     * [select] Executes a select statement and maps the result into a Flow<T> with a given mapper
     */
    inline fun <T: Any>select(
        noinline r2dbcMapper: R2DBCMapper<T>,
        sql: String,
        vararg params: Any,
        crossinline failWith: (Exception) -> E,
    ) = effect<E, Flow<T>> {
        try {
            val stmt = createStatement(sql)
            params.forEachIndexed { idx, param ->
                stmt.bind(idx, param)
            }
            stmt.add().execute().awaitFirst()
                .map(r2dbcMapper)
                .asFlow()
        } catch (e: Exception) {
            e.nonFatalOrThrow()
            shift(failWith(e))
        }
    }

    /**
     * [execute] executes an insert/update/delete and returns the amount of affected rows
     */
    inline fun <E>execute(
        sql: String,
        vararg params: Any,
        crossinline failWith: (Exception) -> E,
    ) = effect<E, Long> {
        try {

            val stmt = createStatement(sql)
            params.forEachIndexed { idx, param ->
                stmt.bind(idx, param)
            }

            stmt.add().execute().awaitFirst()
                .rowsUpdated
                .awaitFirst()
        } catch (e: Exception) {
            e.nonFatalOrThrow()
            shift(failWith(e))
        }
    }

    inline fun executeR2DBC(sql: String) = execute<R2DBCError>(sql) {
        (it as R2dbcException).toTypedError()
    }
}

// TODO add some proper mapping here
fun R2dbcException.toTypedError(): R2DBCError = when (this) {
        is R2dbcException -> R2DBCError.TransactionError(this.message ?: "Unexpected error")
        else -> R2DBCError.Error
}
