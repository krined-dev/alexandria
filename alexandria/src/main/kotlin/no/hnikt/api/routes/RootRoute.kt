package no.hnikt.api.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.root() {
    routing {
        get("/") {
            call.respond(
                message = "Welcome",
                status = HttpStatusCode.OK
            )
        }
    }
}