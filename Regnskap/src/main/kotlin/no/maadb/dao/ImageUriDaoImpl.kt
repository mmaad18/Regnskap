package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.ImageUri
import no.maadb.models.ImageUris
import org.jetbrains.exposed.sql.*

class ImageUriDaoImpl : ImageUriDao {
    override suspend fun byId(id: Long): ImageUri? = dbQuery {
        ImageUris
            .select { ImageUris.id eq id }
            .map(::resultRowToImageUri)
            .singleOrNull()
    }

    override suspend fun all(): List<ImageUri> = dbQuery {
        ImageUris
            .selectAll()
            .map(::resultRowToImageUri)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        ImageUris
            .deleteWhere { ImageUris.id eq id } > 0
    }

    override suspend fun add(receiptId: Long, uri: String): ImageUri? = dbQuery {
        val insertStatement = ImageUris.insert {
            it[ImageUris.receiptId] = receiptId
            it[ImageUris.uri] = uri
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToImageUri)
    }

    override suspend fun edit(id: Long, receiptId: Long, uri: String): Boolean = dbQuery {
        ImageUris.update({ ImageUris.id eq id }) {
            it[ImageUris.receiptId] = receiptId
            it[ImageUris.uri] = uri
        } > 0
    }

    private fun resultRowToImageUri(row: ResultRow) = ImageUri(
        id = row[ImageUris.id],
        receiptId = row[ImageUris.receiptId],
        uri = row[ImageUris.uri],
    )
}