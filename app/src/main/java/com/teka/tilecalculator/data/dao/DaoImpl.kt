package com.teka.tilecalculator.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.teka.tilecalculator.calculator.MeasurementUnits
import com.teka.tilecalculator.calculator.RoomWithTile
import com.teka.tilecalculator.calculator.Tile
import com.teka.tilecalculator.calculator.TileRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface TileDao : BaseDao<Tile> {

    @Query("SELECT * FROM tiles ORDER BY name ASC")
    fun getAllTiles(): Flow<List<Tile>>

    @Query("SELECT * FROM tiles WHERE id = :id")
    fun getTile(id: Int): Flow<Tile>

    @Query("SELECT * FROM tiles WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchTiles(searchQuery: String): Flow<List<Tile>>

    @Query("DELETE FROM tiles WHERE id = :id")
    suspend fun deleteById(id: Int)
}



@Dao
interface TileRoomDao : BaseDao<TileRoom> {

    @Query("SELECT * FROM rooms ORDER BY name ASC")
    fun getAllRooms(): Flow<List<TileRoom>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    fun getRoom(id: Int): Flow<TileRoom>

    @Transaction
    @Query("SELECT * FROM rooms ORDER BY name ASC")
    fun getAllRoomsWithTiles(): Flow<List<RoomWithTile>>

    @Transaction
    @Query("SELECT * FROM rooms WHERE id = :id")
    fun getRoomWithTile(id: Int): Flow<RoomWithTile>

    @Query("SELECT * FROM rooms WHERE tile_id = :tileId ORDER BY name ASC")
    fun getRoomsByTileId(tileId: Int): Flow<List<TileRoom>>

    @Query("SELECT * FROM rooms WHERE is_calculated = :isCalculated ORDER BY name ASC")
    fun getRoomsByCalculationStatus(isCalculated: Boolean): Flow<List<TileRoom>>

    @Query("DELETE FROM rooms WHERE id = :id")
    suspend fun deleteById(id: Int)
}