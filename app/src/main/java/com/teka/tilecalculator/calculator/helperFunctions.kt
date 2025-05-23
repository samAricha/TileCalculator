package com.teka.tilecalculator.calculator


fun calculateTiles(
    roomLength: Double,
    roomWidth: Double,
    tileLength: Double,
    tileWidth: Double,
    wastagePercent: Int
): Int {
    val roomArea = roomLength * roomWidth
    val tileArea = tileLength * tileWidth

    if (tileArea <= 0) return 0

    val baseTiles = (roomArea / tileArea).toInt()
    val withWastage = baseTiles * (1 + wastagePercent / 100.0)

    return kotlin.math.ceil(withWastage).toInt()
}


fun convertToMeters(value: Double, unit: String): Double {
    return when (unit) {
        "ft" -> value * 0.3048
        "in" -> value * 0.0254
        "cm" -> value * 0.01
        "m" -> value
        else -> value
    }
}