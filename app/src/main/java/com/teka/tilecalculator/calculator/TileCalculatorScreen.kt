package com.teka.tilecalculator.calculator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding


@Composable
fun TileCalculatorScreen() {
    val scrollState = rememberScrollState()

    var roomLength by remember { mutableStateOf("") }
    var roomWidth by remember { mutableStateOf("") }
    var roomUnit by remember { mutableStateOf("m") }

    var tileLength by remember { mutableStateOf("") }
    var tileWidth by remember { mutableStateOf("") }
    var tileUnit by remember { mutableStateOf("in") }

    var wastagePercent by remember { mutableStateOf("10") }
    var result by remember { mutableStateOf<Int?>(null) }

    val tilePresets = listOf(
        "Small Tile (6×6)" to Pair("6", "6"),
        "Standard Tile (12×12)" to Pair("12", "12"),
        "Large Tile (18×18)" to Pair("18", "18"),
        "Plank Tile (6×36)" to Pair("6", "36"),
        "Large Format (24×24)" to Pair("24", "24")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime)
            .verticalScroll(scrollState)
            .padding(20.dp)
            .padding(bottom = 200.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Tile Calculator",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary
        )

        // Room Size Section - no presets
        SizeInputCard(
            title = "Room Dimensions",
            lengthValue = roomLength,
            onLengthChange = { roomLength = it },
            widthValue = roomWidth,
            onWidthChange = { roomWidth = it },
            unit = roomUnit,
            onUnitChange = { roomUnit = it },
            units = listOf("m", "ft", "in"),
            presets = null
        )

        // Tile Size Section - with presets
        SizeInputCard(
            title = "Tile Dimensions",
            lengthValue = tileLength,
            onLengthChange = { tileLength = it },
            widthValue = tileWidth,
            onWidthChange = { tileWidth = it },
            unit = tileUnit,
            onUnitChange = { tileUnit = it },
            units = listOf("in", "ft", "cm"),
            presets = tilePresets
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = wastagePercent,
                    onValueChange = { wastagePercent = it },
                    label = { Text("Waste (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Button(
                    onClick = {
                        // Convert everything to meters(standard) for calculation
                        val roomLengthM = convertToMeters(roomLength.toDoubleOrNull() ?: 0.0, roomUnit)
                        val roomWidthM = convertToMeters(roomWidth.toDoubleOrNull() ?: 0.0, roomUnit)
                        val tileLengthM = convertToMeters(tileLength.toDoubleOrNull() ?: 0.0, tileUnit)
                        val tileWidthM = convertToMeters(tileWidth.toDoubleOrNull() ?: 0.0, tileUnit)
                        val waste = wastagePercent.toIntOrNull() ?: 10

                        result = calculateTiles(roomLengthM, roomWidthM, tileLengthM, tileWidthM, waste)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Calculate", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        result?.let { tileCount ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Result",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "$tileCount tiles needed",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    // room area
                    val roomArea = (roomLength.toDoubleOrNull() ?: 0.0) * (roomWidth.toDoubleOrNull() ?: 0.0)
                    if (roomArea > 0) {
                        Text(
                            text = "Room area: ${String.format("%.1f", roomArea)} sq $roomUnit",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}