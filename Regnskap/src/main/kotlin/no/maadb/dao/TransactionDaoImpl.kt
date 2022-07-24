package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.Transaction
import no.maadb.models.Transactions
import org.jetbrains.exposed.sql.*

class TransactionDaoImpl : TransactionDao {
    override suspend fun byId(id: Long): Transaction? = dbQuery {
        Transactions
            .select { Transactions.id eq id }
            .map(::resultRowToTransaction)
            .singleOrNull()
    }

    override suspend fun byParentId(id: Long): List<Transaction> = dbQuery {
        Transactions
            .selectAll()
            .map(::resultRowToTransaction)
    }

    override suspend fun all(): List<Transaction> = dbQuery {
        Transactions
            .selectAll()
            .map(::resultRowToTransaction)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        Transactions
            .deleteWhere { Transactions.id eq id } > 0
    }

    override suspend fun add(): Transaction? = dbQuery {
        val insertStatement = Transactions.insert {
            it[flowIn] = 0.0
            it[flowOut] = 0.0
            it[balance] = 0.0
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTransaction)
    }

    override suspend fun update(id: Long): Boolean = dbQuery {
        val receipts = ReceiptDaoImpl().byParentId(id)
        val sumIn = receipts.sumOf { it.flowIn }
        val sumOut = receipts.sumOf { it.flowOut }

        Transactions.update({ Transactions.id eq id }) {
            it[flowIn] = sumIn
            it[flowOut] = sumOut
            it[balance] = sumIn - sumOut
        } > 0
    }

    private fun resultRowToTransaction(row: ResultRow) = Transaction(
        id = row[Transactions.id],
    )
}