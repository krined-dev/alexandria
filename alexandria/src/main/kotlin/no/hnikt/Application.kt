package no.hnikt

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.hnikt.api.routes.configureRouting
import no.hnikt.setup.configureMicrometerMetrics
import no.hnikt.setup.configureSerialization
import no.hnikt.utils.fileReader

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::run)
        .start(wait = true)
}

fun Application.run() {
    configureSerialization()
    configureMicrometerMetrics()
    configureRouting()
//    fileReader()
}
