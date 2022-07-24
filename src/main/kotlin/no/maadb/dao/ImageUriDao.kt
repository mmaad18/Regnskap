package no.maadb.dao

import no.maadb.models.ImageUri

interface ImageUriDao : Dao<ImageUri> {
    suspend fun add(receiptId: Long, uri: String): ImageUri?
    suspend fun edit(id: Long, receiptId: Long, uri: String): Boolean
}