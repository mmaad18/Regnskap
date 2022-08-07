package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.maadb.dao.receiptDao
import no.maadb.models.Receipt

fun Route.receiptRouting() {
    route("receipt") {
        get {
            val receipts = receiptDao.all()
            call.respond(HttpStatusCode.OK, receipts)
        }
        post("add") {
            val body = call.receive<Receipt>()
            val receipt = receiptDao.add(body)

            if(receipt == null) {
                call.respond(HttpStatusCode.BadRequest, "Could not create receipt.")
            }
            else {
                call.respond(HttpStatusCode.Created, receipt)
            }
        }
        get("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val receipt = receiptDao.byId(id)
            if(receipt == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find receipt with id: ${id}.")
            }
            else {
                call.respond(HttpStatusCode.OK, receipt)
            }
        }
        post("edit") {
            val body = call.receive<Receipt>()
            val receipt = receiptDao.byId(body.id)
            if(receipt == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find receipt with id: ${body.id}.")
            }
            else {
                if(receiptDao.edit(body)) {
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.BadRequest, "Could not update receipt with id: ${body.id}.")
                }
            }
        }
        delete("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            receiptDao.delete(id)
            call.respondRedirect("/receipt")
        }
    }
}