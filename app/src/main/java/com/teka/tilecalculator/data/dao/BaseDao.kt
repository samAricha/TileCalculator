package com.teka.tilecalculator.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}