package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.ImageUri

class ImageUriDaoImpl : ImageUriDao {
    override suspend fun byId(id: Long): ImageUri? = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun all(): List<ImageUri> = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun add(receiptId: Long, uri: String): ImageUri? = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun edit(id: Long, receiptId: Long, uri: String): Boolean = dbQuery {
        TODO("Not yet implemented")
    }
}