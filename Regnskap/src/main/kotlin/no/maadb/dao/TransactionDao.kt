package no.maadb.dao

import no.maadb.models.Receipt
import no.maadb.models.Transaction

interface TransactionDao : Dao<Transaction> {
    suspend fun add(receipts: List<Receipt>): Transaction?
    suspend fun edit(id: Long, receipts: List<Receipt>): Boolean
}