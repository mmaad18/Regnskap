package no.maadb.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.transactionRouting() {
    route("/transaction") {
        get("/") {
            call.respondText("Hello World?!")
        }
    }
}