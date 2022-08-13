package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.maadb.dao.transactionDao
import no.maadb.models.TransactionDto

fun Route.transactionRouting() {
    route("transaction") {
        get {
            val transactions = transactionDao.all()
            call.respond(HttpStatusCode.OK, transactions)
        }
        post("add") {
            val transaction = transactionDao.add(TransactionDto())

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
        patch("{id}/update") {
            val id = call.parameters.getOrFail<Long>("id")
            val transaction = transactionDao.byId(id)
            if(transaction == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find transaction with id: ${id}.")
            }
            else {
                if(transactionDao.edit(id, TransactionDto())) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.BadRequest, "Could not update transaction with id: ${id}.")
                }
            }
        }
        delete("{id}/delete") {
            val id = call.parameters.getOrFail<Long>("id")
            transactionDao.delete(id)
            call.respondRedirect("/transaction")
        }
    }
}