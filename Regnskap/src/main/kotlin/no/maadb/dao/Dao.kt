package no.maadb.dao

import org.jetbrains.exposed.sql.Table

interface Dao<T: Any> {
    suspend fun byId(id: Long): T?
    suspend fun all(): List<T>
    suspend fun delete(id: Long): Boolean
}