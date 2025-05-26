package com.teka.tilecalculator.data.repository

import com.teka.tilecalculator.data.dao.BaseDao
import kotlinx.coroutines.flow.Flow

abstract class BaseRepositoryImpl<T, ID>(
    protected val dao: BaseDao<T>
) : BaseRepository<T, ID> {

    abstract override fun getAll(): Flow<List<T>>
    abstract override fun getById(id: ID): Flow<T?>
    override suspend fun insert(entity: T) = dao.insert(entity)
    override suspend fun update(entity: T) = dao.update(entity)
    override suspend fun delete(entity: T) = dao.delete(entity)
}

