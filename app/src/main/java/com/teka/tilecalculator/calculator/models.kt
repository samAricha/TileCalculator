package com.teka.tilecalculator.calculator

enum class measurementUnits(
    val unitName: String,
    val shortRep: String
) {
    INCHES("Inches", "in"),
    FEET("Feet", "ft"),
    METERS("Meters", "m")
}

data class Tile(
    val length: Double,
    val width: Double,
    val lengthUnit: measurementUnits,
    val widthUnit: measurementUnits,
    val wastePercent: Int,
    val boxSize: Int
)

data class Room(
    val name: String,
    val tile: Tile,
    val length: Double,
    val width: Double,
    val lengthUnit: measurementUnits,
    val widthUnit: measurementUnits
)


val initTileList = listOf(
    Tile(
        length = 12.0,
        width = 12.0,
        lengthUnit = measurementUnits.INCHES,
        widthUnit = measurementUnits.INCHES,
        wastePercent = 10,
        boxSize = 20
    ),
    Tile(
        length = 6.0,
        width = 12.0,
        lengthUnit = measurementUnits.INCHES,
        widthUnit = measurementUnits.INCHES,
        wastePercent = 15,
        boxSize = 25
    ),
    Tile(
        length = 18.0,
        width = 18.0,
        lengthUnit = measurementUnits.INCHES,
        widthUnit = measurementUnits.INCHES,
        wastePercent = 8,
        boxSize = 15
    ),
    Tile(
        length = 2.0,
        width = 1.0,
        lengthUnit = measurementUnits.FEET,
        widthUnit = measurementUnits.FEET,
        wastePercent = 12,
        boxSize = 30
    ),
    Tile(
        length = 0.3,
        width = 0.6,
        lengthUnit = measurementUnits.METERS,
        widthUnit = measurementUnits.METERS,
        wastePercent = 5,
        boxSize = 40
    )
)
