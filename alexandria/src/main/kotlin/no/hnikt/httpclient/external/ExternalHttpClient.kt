package no.hnikt.httpclient.external

import ExternalHttpRequest
import ExternalHttpResponse
import arrow.core.Either
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import no.hnikt.domain.models.ClientConfiguration

suspend fun getInformation(
    request: ExternalHttpRequest,
    client: HttpClient,
    clientConfig: ClientConfiguration
) : Either<Throwable, ExternalHttpResponse> = Either.catch {
    client.get(clientConfig.url) {
        setBody(request)
    }.body<ExternalHttpResponse>()
}.mapLeft {it}