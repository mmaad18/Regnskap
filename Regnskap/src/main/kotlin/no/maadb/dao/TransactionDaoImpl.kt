package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.Receipt
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

    override suspend fun all(): List<Transaction> = dbQuery {
        Transactions
            .selectAll()
            .map(::resultRowToTransaction)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        Transactions
            .deleteWhere { Transactions.id eq id } > 0
    }

    override suspend fun add(receipts: List<Receipt>): Transaction? = dbQuery {
        val insertStatement = Transactions.insert {
            it[Transactions.receipts] = receipts.toString()
            it[Transactions.flowIn] = receipts.sumOf { it.flowIn }
            it[Transactions.flowOut] = receipts.sumOf { it.flowOut }
            it[Transactions.balance] = it[Transactions.flowIn] - it[Transactions.flowOut]
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTransaction)
    }

    override suspend fun edit(id: Long, receipts: List<Receipt>): Boolean = dbQuery {
        Transactions.update({ Transactions.id eq id }) {
            it[Transactions.receipts] = receipts.toString()
        } > 0
    }

    private fun resultRowToTransaction(row: ResultRow) = Transaction(
        id = row[Transactions.id],
        receipts = listOf(row[Transactions.receipts] as Receipt),
    )
}