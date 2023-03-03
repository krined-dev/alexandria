package no.hnikt.repository

import no.hnikt.domain.models.*

/** * [InternalHttpClient] * Interface that describes the methods we need to send messages to * the registry about state updates */
interface InternalHttpClient {
    fun sendUpdatedDataNotification(updatedId: CodeTypes) : ExternalHttpRequest
}