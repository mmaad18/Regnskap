package no.maadb.dao

import no.maadb.models.Receipt

interface ReceiptDao : Dao<Receipt> {
    suspend fun add(transactionId: Long, flowIn: Double, flowOut: Double, details: String, imageUris: List<String>): Receipt?
    suspend fun edit(id: Long, transactionId: Long, flowIn: Double, flowOut: Double, details: String, imageUris: List<String>): Boolean
}