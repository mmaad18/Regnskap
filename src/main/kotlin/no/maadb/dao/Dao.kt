package no.maadb.dao

interface Dao<T: Any> {
    suspend fun byId(id: Long): T?
    suspend fun byParentId(id: Long): List<T>
    suspend fun all(): List<T>
    suspend fun add(model: T): T?
    suspend fun edit(model: T): Boolean
    suspend fun delete(id: Long): Boolean
}