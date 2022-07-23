package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.Receipt
import no.maadb.models.Transaction

class TransactionDaoImpl : TransactionDao {
    override suspend fun byId(id: Long): Transaction? = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun all(): List<Transaction> = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun add(receipts: List<Receipt>): Transaction? = dbQuery {
        TODO("Not yet implemented")
    }

    override suspend fun edit(id: Long, receipts: List<Receipt>): Boolean = dbQuery {
        TODO("Not yet implemented")
    }
}