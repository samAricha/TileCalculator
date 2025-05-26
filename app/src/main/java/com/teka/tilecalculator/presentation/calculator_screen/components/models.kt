package com.teka.tilecalculator.presentation.calculator_screen.components

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class MeasurementUnits(
    val unitName: String,
    val shortRep: String
) {
    INCHES("Inches", "in"),
    FEET("Feet", "ft"),
    METERS("Meters", "m")
}

@Entity(tableName = "tiles")
data class Tile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "length")
    val length: Double,

    @ColumnInfo(name = "width")
    val width: Double,

    @ColumnInfo(name = "length_unit")
    val lengthUnit: MeasurementUnits,

    @ColumnInfo(name = "width_unit")
    val widthUnit: MeasurementUnits,

    @ColumnInfo(name = "waste_percent")
    val wastePercent: Int,

    @ColumnInfo(name = "box_size")
    val boxSize: Int,

    @ColumnInfo(name = "price_per_box")
    val pricePerBox: Double = 0.0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)


@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = Tile::class,
            parentColumns = ["id"],
            childColumns = ["tile_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TileRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "tile_id")
    val tileId: Int,

    @ColumnInfo(name = "length")
    val length: Double,

    @ColumnInfo(name = "width")
    val width: Double,

    @ColumnInfo(name = "length_unit")
    val lengthUnit: MeasurementUnits,

    @ColumnInfo(name = "width_unit")
    val widthUnit: MeasurementUnits,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_calculated")
    val isCalculated: Boolean = false
)


data class RoomWithTile(
    @Embedded val room: TileRoom,
    @Relation(
        parentColumn = "tile_id",
        entityColumn = "id"
    )
    val tile: Tile
)



data class TileBoxCounts(
    val tileCount: Int,
    val boxCount: Int
)



val initTileList = listOf(
    Tile(
        length = 12.0,
        width = 12.0,
        lengthUnit = MeasurementUnits.INCHES,
        widthUnit = MeasurementUnits.INCHES,
        wastePercent = 10,
        boxSize = 20
    ),
    Tile(
        length = 6.0,
        width = 12.0,
        lengthUnit = MeasurementUnits.INCHES,
        widthUnit = MeasurementUnits.INCHES,
        wastePercent = 15,
        boxSize = 25
    ),
    Tile(
        length = 18.0,
        width = 18.0,
        lengthUnit = MeasurementUnits.INCHES,
        widthUnit = MeasurementUnits.INCHES,
        wastePercent = 8,
        boxSize = 15
    ),
    Tile(
        length = 2.0,
        width = 1.0,
        lengthUnit = MeasurementUnits.FEET,
        widthUnit = MeasurementUnits.FEET,
        wastePercent = 12,
        boxSize = 30
    ),
    Tile(
        length = 0.3,
        width = 0.6,
        lengthUnit = MeasurementUnits.METERS,
        widthUnit = MeasurementUnits.METERS,
        wastePercent = 5,
        boxSize = 40
    )
)
