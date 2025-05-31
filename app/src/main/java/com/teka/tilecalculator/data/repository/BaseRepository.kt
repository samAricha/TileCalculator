package com.teka.tilecalculator.data.repository

import kotlinx.coroutines.flow.Flow

interface BaseRepository<T, ID> {
    fun getAll(): Flow<List<T>>
    fun getById(id: ID): Flow<T?>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
}

