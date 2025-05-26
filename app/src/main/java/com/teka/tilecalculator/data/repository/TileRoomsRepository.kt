package com.teka.tilecalculator.data.repository

import com.teka.tilecalculator.calculator.RoomWithTile
import com.teka.tilecalculator.calculator.TileRoom
import com.teka.tilecalculator.data.dao.TileRoomDao
import kotlinx.coroutines.flow.Flow

interface TileRoomsRepository : BaseRepository<TileRoom, Int> {

    fun getAllRoomsWithTilesStream(): Flow<List<RoomWithTile>>

    fun getRoomWithTileStream(id: Int): Flow<RoomWithTile>

    fun getRoomsByTileIdStream(tileId: Int): Flow<List<TileRoom>>

    fun getRoomsByCalculationStatusStream(isCalculated: Boolean): Flow<List<TileRoom>>

    suspend fun deleteRoomById(id: Int)
}

class TileRoomsRepositoryImpl(
    private val roomDao: TileRoomDao
) : BaseRepositoryImpl<TileRoom, Int>(roomDao), TileRoomsRepository {

    override fun getAll(): Flow<List<TileRoom>> = roomDao.getAllRooms()

    override fun getById(id: Int): Flow<TileRoom?> = roomDao.getRoom(id)

    override fun getAllRoomsWithTilesStream(): Flow<List<RoomWithTile>> =
        roomDao.getAllRoomsWithTiles()

    override fun getRoomWithTileStream(id: Int): Flow<RoomWithTile> =
        roomDao.getRoomWithTile(id)

    override fun getRoomsByTileIdStream(tileId: Int): Flow<List<TileRoom>> =
        roomDao.getRoomsByTileId(tileId)

    override fun getRoomsByCalculationStatusStream(isCalculated: Boolean): Flow<List<TileRoom>> =
        roomDao.getRoomsByCalculationStatus(isCalculated)

    override suspend fun deleteRoomById(id: Int) = roomDao.deleteById(id)
}