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
    val lengthUnit: String,
    val widthUnit: String
)