package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.maadb.dao.imageUriDao
import no.maadb.models.ImageUriDto

fun Route.imageUriRouting() {
    route("imageuri") {
        get {
            val imageUris = imageUriDao.all()
            call.respond(HttpStatusCode.OK, imageUris)
        }
        post("add") {
            val body = call.receive<ImageUriDto>()
            if (isReceipt(body.receiptId, call)) {
                val imageUri = imageUriDao.add(body)
                if (imageUri == null) {
                    call.respond(HttpStatusCode.BadRequest, "Could not create image uri.")
                } else {
                    call.respond(HttpStatusCode.Created, imageUri)
                }
            }
        }
        get("{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val imageUri = imageUriDao.byId(id)
            if (imageUri == null) {
                call.respond(HttpStatusCode.NotFound, "Could not find image uri with id: ${id}.")
            } else {
                call.respond(HttpStatusCode.OK, imageUri)
            }
        }
        get("parent/{id}") {
            val id = call.parameters.getOrFail<Long>("id")
            val imageUris = imageUriDao.byParentId(id)
            call.respond(HttpStatusCode.OK, imageUris)
        }
        patch("{id}/edit") {
            val id = call.parameters.getOrFail<Long>("id")
            val body = call.receive<ImageUriDto>()
            if (isReceipt(body.receiptId, call)) {
                val imageUri = imageUriDao.byId(id)
                if (imageUri == null) {
                    call.respond(HttpStatusCode.NotFound, "Could not find image uri with id: ${id}.")
                } else {
                    if (imageUriDao.edit(id, body)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Could not update image uri with id: ${id}.")
                    }
                }
            }
        }
        delete("{id}/delete") {
            val id = call.parameters.getOrFail<Long>("id")
            imageUriDao.delete(id)
            call.respondRedirect("/imageuri")
        }
    }
}