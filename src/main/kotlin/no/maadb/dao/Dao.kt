package no.maadb.dao

interface Dao<T: Any> {
    suspend fun byId(id: Long): T?
    suspend fun byParentId(id: Long): List<T>
    suspend fun all(): List<T>
    suspend fun delete(id: Long): Boolean
}