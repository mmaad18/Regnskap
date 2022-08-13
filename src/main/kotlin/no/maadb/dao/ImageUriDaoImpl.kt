package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.ImageUri
import no.maadb.models.ImageUriDto
import no.maadb.models.ImageUris
import org.jetbrains.exposed.sql.*

class ImageUriDaoImpl : ImageUriDao {
    override suspend fun byId(id: Long): ImageUri? = dbQuery {
        ImageUris
            .select { ImageUris.id eq id }
            .map(::resultRowToImageUri)
            .singleOrNull()
    }

    override suspend fun byParentId(parentId: Long): List<ImageUri> = dbQuery {
        ImageUris
            .select { ImageUris.receiptId eq parentId }
            .map(::resultRowToImageUri)
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

    override suspend fun add(dto: ImageUriDto): ImageUri? = dbQuery {
        val insertStatement = ImageUris.insert {
            it[receiptId] = dto.receiptId
            it[uri] = dto.uri
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToImageUri)
    }

    override suspend fun edit(id: Long, dto: ImageUriDto): Boolean = dbQuery {
        ImageUris.update({ ImageUris.id eq id }) {
            it[receiptId] = dto.receiptId
            it[uri] = dto.uri
        } > 0
    }

    private fun resultRowToImageUri(row: ResultRow) = ImageUri(
        id = row[ImageUris.id],
        receiptId = row[ImageUris.receiptId],
        uri = row[ImageUris.uri],
    )
}

val imageUriDao: ImageUriDao = ImageUriDaoImpl()