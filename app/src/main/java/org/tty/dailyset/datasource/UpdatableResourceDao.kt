package org.tty.dailyset.datasource

interface UpdatableResourceDao {
    suspend fun update(item: Any)
    suspend fun updateBatch(items: List<*>)
}