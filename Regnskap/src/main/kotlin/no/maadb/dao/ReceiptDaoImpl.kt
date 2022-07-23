package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.Receipt

class ReceiptDaoImpl : ReceiptDao {
    override suspend fun byId(id: Long): Receipt? = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun all(): List<Receipt> = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun add(
        transactionId: Long,
        flowIn: Double,
        flowOut: Double,
        details: String,
        imageUris: List<String>
    ): Receipt? = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun edit(
        id: Long,
        transactionId: Long,
        flowIn: Double,
        flowOut: Double,
        details: String,
        imageUris: List<String>
    ): Boolean = dbQuery {
        TODO("Not yet implemented")
    }
}