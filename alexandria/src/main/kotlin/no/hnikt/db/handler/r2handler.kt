package no.hnikt.db.handler

import arrow.core.continuations.Effect
import arrow.core.continuations.effect
import io.r2dbc.spi.*
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
    object TransactionError : R2DBCError
}

object R2DBC {
    inline fun <E, T>ConnectionFactory.transaction(
        failWith: E,
        crossinline handler: context(Connection) () -> Effect<R2DBCError, T>): Effect<E, T> = effect {
        val connection = create().awaitFirst()
        try {
            val value = handler(connection)
            connection.commitTransaction()
            value.fold({
                connection.rollbackTransaction()
                shift(failWith)
            }, { it })
        } catch (e: Exception) {
            connection.rollbackTransaction()
            if (e is kotlinx.coroutines.CancellationException) {
                throw e
            }
            shift(failWith)
        }
    }
}


/**
 * [execute] executes an insert/update/delete and returns the amount of affected rows
 */
context (Connection)
fun execute(sql: String, vararg params: Any) = effect<R2DBCError, Long> {
    val stmt = createStatement(sql)
    params.forEachIndexed { idx, param ->
        stmt.bind(idx, param)
    }

    stmt.add().execute().awaitFirst()
        .rowsUpdated
        .awaitFirst()
}

/**
 * [select] Executes a select statement and maps the result into a Flow<T> with a given mapper
 */
context (Connection)
inline fun <reified T: Any>select(sql: String,  noinline r2dbcMapper: R2DBCMapper<T>, vararg params: Any) = effect<R2DBCError, Flow<T>> {
    val stmt = createStatement(sql)

    params.forEachIndexed { idx, param ->
        stmt.bind(idx, param)
    }

    stmt.add().execute().awaitFirst().map(r2dbcMapper).asFlow()
}
