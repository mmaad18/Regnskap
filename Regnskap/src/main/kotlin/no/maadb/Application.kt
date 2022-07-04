package no.maadb

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import no.maadb.plugins.configureRouting
import no.maadb.plugins.configureSerialization

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        anyHost()
    }

    configureRouting()
    configureSerialization()
}
