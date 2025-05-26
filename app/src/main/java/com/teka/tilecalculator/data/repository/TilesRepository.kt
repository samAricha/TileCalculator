package com.teka.tilecalculator.data.repository

import com.teka.tilecalculator.presentation.calculator_screen.components.Tile
import com.teka.tilecalculator.data.dao.TileDao
import com.teka.tilecalculator.presentation.calculator_screen.components.initTileList
import kotlinx.coroutines.flow.Flow

interface TilesRepository : BaseRepository<Tile, Int> {

    suspend fun initializeIfNeeded()

    fun searchTiles(searchQuery: String): Flow<List<Tile>>

    suspend fun deleteTileById(id: Int)
}

class TilesRepositoryImpl(
    private val tileDao: TileDao
) : BaseRepositoryImpl<Tile, Int>(tileDao), TilesRepository {

    override suspend fun initializeIfNeeded() {
        val existingTiles = tileDao.getCount()

        if (existingTiles == 0) {
            // if Database is empty, insert initial tiles
            initTileList.forEach { tile ->
                tileDao.insert(tile)
            }
        }
    }

    override fun getAll(): Flow<List<Tile>> = tileDao.getAllTiles()

    override fun getById(id: Int): Flow<Tile?> = tileDao.getTile(id)

    override fun searchTiles(searchQuery: String): Flow<List<Tile>> =
        tileDao.searchTiles(searchQuery)

    override suspend fun deleteTileById(id: Int) = tileDao.deleteById(id)
}