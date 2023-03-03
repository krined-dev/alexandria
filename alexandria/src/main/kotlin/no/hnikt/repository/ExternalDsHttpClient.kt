package no.hnikt.repository

import no.hnikt.domain.models.*

/** * [ExternalDsHttpClient] * Interface describing the methods we need to retrieve data * from an external HTTP data source */
interface ExternalDsHttpClient {
    fun retrieveDataFromExternal(source: Source, request: ExternalHttpRequest): ExternalHttpResponse
}