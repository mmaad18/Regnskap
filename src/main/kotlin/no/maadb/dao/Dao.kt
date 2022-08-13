package no.maadb.dao

interface Dao<T: Any, V: Any> {
    suspend fun byId(id: Long): T?
    suspend fun byParentId(parentId: Long): List<T>
    suspend fun all(): List<T>
    suspend fun add(dto: V): T?
    suspend fun edit(id: Long, dto: V): Boolean
    suspend fun delete(id: Long): Boolean
}