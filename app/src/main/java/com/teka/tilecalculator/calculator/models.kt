package com.teka.tilecalculator.calculator

enum class measurementUnits { inches, feet, meters }

data class Tile(
    val length: Double,
    val width: Double,
    val lengthUnit: measurementUnits,
    val widthUnit: measurementUnits
)

data class Room(
    val name: String,
    val tile: Tile,
    val length: Double,
    val width: Double,
    val lengthUnit: String,
    val widthUnit: String
)