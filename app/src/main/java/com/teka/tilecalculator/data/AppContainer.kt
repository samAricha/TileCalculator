package com.teka.tilecalculator.data

import android.content.Context
import com.teka.tilecalculator.data.dao.TileRoomDao
import com.teka.tilecalculator.data.repository.TileRoomsRepository
import com.teka.tilecalculator.data.repository.TileRoomsRepositoryImpl
import com.teka.tilecalculator.data.repository.TilesRepository
import com.teka.tilecalculator.data.repository.TilesRepositoryImpl


interface AppContainer {
    val tilesRepository: TilesRepository
    val tileRoomsRepository: TileRoomsRepository
}


class AppDataContainer(private val context: Context) : AppContainer {
    override val tilesRepository: TilesRepository by lazy {
        TilesRepositoryImpl(TileCalculatorDatabase.getDatabase(context).tileDao())
    }


    override val tileRoomsRepository: TileRoomsRepository by lazy {
        TileRoomsRepositoryImpl(TileCalculatorDatabase.getDatabase(context).tileRoomDao())
    }

}

