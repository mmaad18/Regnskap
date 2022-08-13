package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.Receipt
import no.maadb.models.Receipts
import org.jetbrains.exposed.sql.*

class ReceiptDaoImpl : ReceiptDao {
    override suspend fun byId(id: Long): Receipt? = dbQuery {
        Receipts
            .select { Receipts.id eq id }
            .map(::resultRowToReceipt)
            .singleOrNull()
    }

    override suspend fun byParentId(id: Long): List<Receipt> = dbQuery {
        Receipts
            .select { Receipts.transactionId eq id }
            .map(::resultRowToReceipt)
    }

    override suspend fun all(): List<Receipt> = dbQuery {
        Receipts
            .selectAll()
            .map(::resultRowToReceipt)
    }

    override suspend fun delete(id: Long): Boolean = dbQuery {
        Receipts
            .deleteWhere { Receipts.id eq id } > 0
    }

    override suspend fun add(
        transactionId: Long,
        flowIn: Double,
        flowOut: Double,
        details: String
    ): Receipt? = dbQuery {
        val insertStatement = Receipts.insert {
            it[Receipts.transactionId] = transactionId
            it[Receipts.flowIn] = flowIn
            it[Receipts.flowOut] = flowOut
            it[balance] = flowIn - flowOut
            it[Receipts.details] = details
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToReceipt)
    }

    override suspend fun add(receipt: Receipt): Receipt? = dbQuery {
        val insertStatement = Receipts.insert {
            it[transactionId] = receipt.transactionId
            it[flowIn] = receipt.flowIn
            it[flowOut] = receipt.flowOut
            it[balance] = receipt.flowIn - receipt.flowOut
            it[details] = receipt.details
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToReceipt)
    }

    override suspend fun edit(
        id: Long,
        transactionId: Long,
        flowIn: Double,
        flowOut: Double,
        details: String
    ): Boolean = dbQuery {
        Receipts.update({ Receipts.id eq id }) {
            it[Receipts.transactionId] = transactionId
            it[Receipts.flowIn] = flowIn
            it[Receipts.flowOut] = flowOut
            it[balance] = flowIn - flowOut
            it[Receipts.details] = details
        } > 0
    }

    override suspend fun edit(receipt: Receipt): Boolean = dbQuery {
        Receipts.update({ Receipts.id eq receipt.id }) {
            it[transactionId] = receipt.transactionId
            it[flowIn] = receipt.flowIn
            it[flowOut] = receipt.flowOut
            it[balance] = receipt.flowIn - receipt.flowOut
            it[details] = receipt.details
        } > 0
    }

    private fun resultRowToReceipt(row: ResultRow) = Receipt(
        id = row[Receipts.id],
        transactionId = row[Receipts.transactionId],
        flowIn = row[Receipts.flowIn],
        flowOut = row[Receipts.flowOut],
        balance = row[Receipts.balance],
        details = row[Receipts.details],
    )
}

val receiptDao: ReceiptDao = ReceiptDaoImpl()