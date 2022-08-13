package no.maadb.dao

import no.maadb.dao.DatabaseFactory.dbQuery
import no.maadb.models.Receipt
import no.maadb.models.ReceiptDto
import no.maadb.models.Receipts
import org.jetbrains.exposed.sql.*

class ReceiptDaoImpl : ReceiptDao {
    override suspend fun byId(id: Long): Receipt? = dbQuery {
        Receipts
            .select { Receipts.id eq id }
            .map(::resultRowToReceipt)
            .singleOrNull()
    }

    override suspend fun byParentId(parentId: Long): List<Receipt> = dbQuery {
        Receipts
            .select { Receipts.transactionId eq parentId }
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

    override suspend fun add(dto: ReceiptDto): Receipt? = dbQuery {
        val insertStatement = Receipts.insert {
            it[transactionId] = dto.transactionId
            it[flowIn] = dto.flowIn
            it[flowOut] = dto.flowOut
            it[balance] = dto.flowIn - dto.flowOut
            it[details] = dto.details
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToReceipt)
    }

    override suspend fun edit(id: Long, dto: ReceiptDto): Boolean = dbQuery {
        Receipts.update({ Receipts.id eq id }) {
            it[transactionId] = dto.transactionId
            it[flowIn] = dto.flowIn
            it[flowOut] = dto.flowOut
            it[balance] = dto.flowIn - dto.flowOut
            it[details] = dto.details
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