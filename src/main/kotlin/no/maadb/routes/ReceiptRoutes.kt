package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.maadb.dao.receiptDao
import no.maadb.dao.transactionDao
import no.maadb.models.ReceiptDto

fun Route.receiptRouting() {
    suspend fun updateTransaction(id: Long, call: ApplicationCall): Boolean {
        val transaction = transactionDao.byId(id)

        return if (transaction == null) {
            call.respond(HttpStatusCode.NotFound, "Could not find transaction with id: ${id}.")
            false
        } else {
            transactionDao.edit(id)
            true
        }
    }

    route("receipt") {
        get {
            val receipts = receiptDao.all()
            call.respond(HttpStatusCode.OK, receipts)
        }
        post("add") {
            val body = call.receive<ReceiptDto>()
            if (updateTransaction(body.transactionId, call)) {
                val receipt = receiptDao.add(body)
                if (receipt == null) {
                    call.respond(HttpStatusCode.BadRequest, "Could not create receipt.")
                } else {
                    call.respond(HttpStatusCode.Created, receipt)
                }
            }
        }
        get("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val receipt = receiptDao.byId(id)
            if (receipt == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find receipt with id: ${id}.")
            } else {
                call.respond(HttpStatusCode.OK, receipt)
            }
        }
        get("parent/{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val receipts = receiptDao.byParentId(id)
            call.respond(HttpStatusCode.OK, receipts)
        }
        patch("{id}/edit") {
            val id = call.parameters.getOrFail<Long>("id")
            val body = call.receive<ReceiptDto>()
            if (updateTransaction(body.transactionId, call)) {
                val receipt = receiptDao.byId(id)
                if (receipt == null) {
                    call.respond(HttpStatusCode.NotFound, "Could not find receipt with id: ${id}.")
                } else {
                    if (receiptDao.edit(id, body)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Could not update receipt with id: ${id}.")
                    }
                }
            }
        }
        delete("{id}/delete") {
            val id = call.parameters.getOrFail<Long>("id")
            val receipt = receiptDao.byId(id)
            if (receipt == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find receipt with id: ${id}.")
            } else {
                receiptDao.delete(id)
                updateTransaction(receipt.transactionId, call)
            }
            call.respondRedirect("/receipt")
        }
    }
}