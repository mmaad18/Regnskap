package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.maadb.dao.transactionDao
import no.maadb.models.Transaction

fun Route.transactionRouting() {
    route("transaction") {
        get {
            val transactions = transactionDao.all()
            call.respond(HttpStatusCode.OK, transactions)
        }
        post("add") {
            val transaction = transactionDao.add()

            if(transaction == null) {
                call.respond(HttpStatusCode.BadRequest, "Could not create transaction.")
            }
            else {
                call.respond(HttpStatusCode.Created, transaction)
            }
        }
        get("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val transaction = transactionDao.byId(id)
            if(transaction == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find transaction with id: ${id}.")
            }
            else {
                call.respond(HttpStatusCode.OK, transaction)
            }
        }
        patch("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val transaction = transactionDao.byId(id)
            if(transaction == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find transaction with id: ${id}.")
            }
            else {
                if(transactionDao.update(id)) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.BadRequest, "Could not update transaction with id: ${id}.")
                }
            }
        }
        patch("edit") {
            val body = call.receive<Transaction>()
            val transaction = transactionDao.byId(body.id)
            if(transaction == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find transaction with id: ${body.id}.")
            }
            else {
                if(transactionDao.edit(body)) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.BadRequest, "Could not update transaction with id: ${body.id}")
                }
            }
        }
        delete("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            transactionDao.delete(id)
            call.respondRedirect("/transaction")
        }
    }
}