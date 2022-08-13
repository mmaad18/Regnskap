package no.maadb.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.maadb.routes.imageUriRouting
import no.maadb.routes.receiptRouting
import no.maadb.routes.transactionRouting

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Home Page")
        }

        imageUriRouting()
        receiptRouting()
        transactionRouting()
    }
}
