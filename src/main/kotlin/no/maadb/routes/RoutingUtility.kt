package no.maadb.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import no.maadb.dao.receiptDao
import no.maadb.dao.transactionDao
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO

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

suspend fun writeBase64Image(encodedImage: String, uri: String) {
    val imageByteArray = Base64.getDecoder().decode(encodedImage)
    val image = ImageIO.read(ByteArrayInputStream(imageByteArray))

    ImageIO.write(image, "png", File(uri))
}

suspend fun encodeImageBase64(uri: String): String {
    val bufferedImage = ImageIO.read(File(uri))
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", byteArrayOutputStream)

    val encodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
    byteArrayOutputStream.close()
    return encodedImage
}