package no.maadb.dao

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.ImageUri
import no.maadb.models.ImageUriDto
import no.maadb.models.ImageUris
import org.jetbrains.exposed.sql.*

class ImageUriDaoImpl : ImageUriDao {
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val imagePath = appConfig.property("image.path").getString()

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
        val possibleId = all().size + 1

        val insertStatement = ImageUris.insert {
            it[receiptId] = dto.receiptId
            it[uri] = imagePath + "receipt_${dto.receiptId}_image_${possibleId}.png"
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToImageUri)
    }

    override suspend fun edit(id: Long, dto: ImageUriDto): Boolean = dbQuery {
        ImageUris.update({ ImageUris.id eq id }) {
            it[receiptId] = dto.receiptId
            it[uri] = imagePath + "receipt_${dto.receiptId}_image_${id}.png"
        } > 0
    }

    private fun resultRowToImageUri(row: ResultRow) = ImageUri(
        id = row[ImageUris.id],
        receiptId = row[ImageUris.receiptId],
        uri = row[ImageUris.uri],
    )
}

val imageUriDao: ImageUriDao = ImageUriDaoImpl()