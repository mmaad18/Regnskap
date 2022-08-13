package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import no.maadb.dao.receiptDao
import no.maadb.dao.transactionDao

suspend fun isReceipt(id: Long, call: ApplicationCall): Boolean {
    val receipt = receiptDao.byId(id)

    return if (receipt == null) {
        call.respond(HttpStatusCode.NotFound, "Could not find receipt with id: ${id}.")
        false
    } else {
        true
    }
}

suspend fun isTransaction(id: Long, call: ApplicationCall): Boolean {
    val transaction = transactionDao.byId(id)

    return if (transaction == null) {
        call.respond(HttpStatusCode.NotFound, "Could not find transaction with id: ${id}.")
        false
    } else {
        true
    }
}