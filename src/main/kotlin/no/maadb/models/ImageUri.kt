package no.maadb.models

import org.jetbrains.exposed.sql.*

data class ImageUri(val id: Long, val receiptId: Long, val uri: String)

object ImageUris : Table() {
    val id = long("id").autoIncrement()
    val receiptId = long("receipt_id")
    val uri = varchar("uri", 1024)
}
