package no.hnikt.repository

/** * [RefreshService] The refresh service is an integral part of the application * it serves as the service that starts refresh jobs for data codes. The Service * also triggers updated data notifications to the registry. * Runs as a long-running service. */
interface RefreshService {
    fun runService()
}