package com.teka.tilecalculator.calculator


fun calculateTiles(
    roomLength: Double,
    roomWidth: Double,
    tileLength: Double,
    tileWidth: Double,
    tilesInBox: Int,
    wastagePercent: Int
): TileBoxCounts {
    val roomArea = roomLength * roomWidth
    val tileArea = tileLength * tileWidth

    if (tileArea <= 0) return TileBoxCounts(0, 0)

    val baseTiles = (roomArea / tileArea).toInt()
    val withWastage = baseTiles * (1 + wastagePercent / 100.0)

    val tileCount = kotlin.math.ceil(withWastage).toInt()
    val boxCount = kotlin.math.ceil((tileCount/tilesInBox).toDouble()).toInt()

    return TileBoxCounts(tileCount, boxCount)
}


fun convertToMeters(value: Double, unit: MeasurementUnits): Double {
    return when (unit) {
        MeasurementUnits.FEET -> value * 0.3048
        MeasurementUnits.INCHES -> value * 0.0254
        MeasurementUnits.METERS -> value
    }
}
