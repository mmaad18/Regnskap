package no.maadb.dao

import no.maadb.models.Transaction

interface TransactionDao : Dao<Transaction> {
    suspend fun add(): Transaction?
    suspend fun update(id: Long): Boolean
}