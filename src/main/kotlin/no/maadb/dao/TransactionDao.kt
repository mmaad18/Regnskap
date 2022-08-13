package no.maadb.dao

import no.maadb.models.Transaction
import no.maadb.models.TransactionDto

interface TransactionDao : Dao<Transaction, TransactionDto>