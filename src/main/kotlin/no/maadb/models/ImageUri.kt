package no.maadb.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class ImageUri(val id: Long, val receiptId: Long, val uri: String)

object ImageUris : Table() {
    val id = long("id").autoIncrement()
    val receiptId = reference("receipt_id", Receipts.id)
    val uri = varchar("uri", 1024)
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class ImageUriDto(val receiptId: Long, val uri: String)