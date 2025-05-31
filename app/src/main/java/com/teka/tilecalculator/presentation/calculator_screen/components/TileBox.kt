package com.teka.tilecalculator.presentation.calculator_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teka.tilecalculator.ui.theme.Pink40


@Composable
fun TileBox(
    length: Double,
    width: Double,
    lengthUnit: MeasurementUnits,
    widthUnit: MeasurementUnits,
    modifier: Modifier = Modifier
) {
    val lengthInMeters = convertToMeters(length, lengthUnit)
    val widthInMeters = convertToMeters(width, widthUnit)

    // scaling the box we are to display
    val displayScale = 300f
    val lengthDp = (lengthInMeters * displayScale).toFloat()
    val widthDp = (widthInMeters * displayScale).toFloat()

    Box(
        modifier = modifier
            .size(width = widthDp.dp, height = lengthDp.dp)
            .background(
                color = Pink40,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Text(
            text = "${length}${lengthUnit.shortRep} × ${width}${widthUnit.shortRep}",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

