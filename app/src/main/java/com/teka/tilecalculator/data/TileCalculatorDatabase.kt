package com.teka.tilecalculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teka.tilecalculator.calculator.Tile
import com.teka.tilecalculator.calculator.TileRoom
import com.teka.tilecalculator.data.dao.TileDao
import com.teka.tilecalculator.data.dao.TileRoomDao

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Tile::class, TileRoom::class], version = 1, exportSchema = false)
abstract class TileCalculatorDatabase : RoomDatabase() {

    abstract fun tileDao(): TileDao
    abstract fun tileRoomDao(): TileRoomDao

    companion object {
        @Volatile
        private var Instance: TileCalculatorDatabase? = null

        fun getDatabase(context: Context): TileCalculatorDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TileCalculatorDatabase::class.java, "item_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
