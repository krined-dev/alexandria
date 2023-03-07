package no.hnikt.db.handler

import arrow.core.continuations.Effect
import arrow.core.continuations.EffectScope
import arrow.core.continuations.effect
import arrow.core.nonFatalOrThrow
import io.r2dbc.spi.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst

/**
 * Simple wrapper around r2dbc that gives us a handle to the connection
 * selects return a kotlinx Flow<T>.
 * Inspired by https://gist.github.com/ferrerojosh/109f8931223c92ecc24f5a751759e793
 * Using arrow-fx for wrapping io in effect
 */

/**
 * [R2DBCMapper] A lambda that takes row data and maps into a type T.
 * For every type we query we need to define some mapper to T that gives us the select typ
 */
typealias R2DBCMapper<T> = (row: Row, rowMetadata: RowMetadata) -> T
data class TransactionError(val stacktrace: String)

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
     * [select] Selects with default error handling, turns thrown exceptions into a typed TransactionError
     */
    fun <T> select(r2DBCMapper: R2DBCMapper<T>, sql: String, vararg params: Any): Effect<TransactionError, T> =
        select(r2DBCMapper, sql, params, defaultErrorHandler)

    /**
     * [execute] executes an insert/update/delete and returns the amount of affected rows
     */
    inline fun <E> execute(
        sql: String,
        vararg params: Any,
        crossinline failWith: (Exception) -> E
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
            shift(failWith.invoke(e))
        }
    }

    /**
     * executes an insert/update/delete and returns the amount of affected rows. Using default error handling
     * which returns an TransactionError.
     */
    fun execute(sql: String, vararg params: Any): Effect<TransactionError, Long> = execute(sql, params, defaultErrorHandler)
}

private val defaultErrorHandler : (Exception) -> TransactionError = { TransactionError("Transaction failed with:\n${it.stackTraceToString()}")}