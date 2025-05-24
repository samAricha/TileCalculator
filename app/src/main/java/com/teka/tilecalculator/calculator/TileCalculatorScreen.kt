package com.teka.tilecalculator.calculator

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.House
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TileCalculatorScreen() {
    val scrollState = rememberScrollState()
    val screenContext = LocalContext.current

    var roomName by remember { mutableStateOf("") }
    var roomLength by remember { mutableStateOf("") }
    var roomWidth by remember { mutableStateOf("") }
    var roomLengthUnit by remember { mutableStateOf(measurementUnits.INCHES) }
    var roomWidthUnit by remember { mutableStateOf(measurementUnits.INCHES) }

    var tileLength by remember { mutableStateOf("") }
    var tileWidth by remember { mutableStateOf("") }
    var tileLengthUnit by remember { mutableStateOf(measurementUnits.INCHES) }
    var tileWidthUnit by remember { mutableStateOf(measurementUnits.INCHES) }

    var selectedTile by remember { mutableStateOf<Tile?>(null) }
    var tileList: List<Tile> by remember { mutableStateOf<List<Tile>>(initTileList) }
    var roomList by remember { mutableStateOf<List<Room>>(emptyList()) }
    var wastagePercent by remember { mutableStateOf("10") }
    var boxSize by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()
    val tileSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showTileBottomSheet by remember { mutableStateOf(false) }
    val roomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showRoomBottomSheet by remember { mutableStateOf(false) }

    val tilePresets = listOf(
        "Small Tile (6×6)" to Pair("6", "6"),
        "Standard Tile (12×12)" to Pair("12", "12"),
        "Large Tile (18×18)" to Pair("18", "18"),
        "Plank Tile (6×36)" to Pair("6", "36"),
        "Large Format (24×24)" to Pair("24", "24")
    )



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.ime)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { showRoomBottomSheet = !showRoomBottomSheet }
                ) {
                    Icon(Icons.Filled.House, "Create Room.")
                    Text("Create Room")
                }
            }

            LazyRow {
                items(tileList) { tile ->
                    Text(text = "Item: ${tile.width} ${tile.widthUnit.shortRep} * ${tile.length} ${tile.lengthUnit.shortRep}")
                }
            }

            LazyColumn {
                items(roomList) { room ->
                    val roomLengthM =
                        convertToMeters(room.length, room.lengthUnit.shortRep)
                    val roomWidthM =
                        convertToMeters(room.width, room.widthUnit.shortRep)
                    val tileLengthM =
                        convertToMeters(tileLength.toDoubleOrNull() ?: 0.0, tileLengthUnit.shortRep)
                    val tileWidthM =
                        convertToMeters(tileWidth.toDoubleOrNull() ?: 0.0, tileWidthUnit.shortRep)
                    val waste = wastagePercent.toIntOrNull() ?: 10




                    Text(text = "Item: ${room.name}")
                }
            }


            Button(
                onClick = {
                    // Convert everything to meters(standard) for calculation
                    val roomLengthM =
                        convertToMeters(roomLength.toDoubleOrNull() ?: 0.0, tileLengthUnit.shortRep)
                    val roomWidthM =
                        convertToMeters(roomWidth.toDoubleOrNull() ?: 0.0, tileWidthUnit.shortRep)
                    val tileLengthM =
                        convertToMeters(tileLength.toDoubleOrNull() ?: 0.0, tileLengthUnit.shortRep)
                    val tileWidthM =
                        convertToMeters(tileWidth.toDoubleOrNull() ?: 0.0, tileWidthUnit.shortRep)
                    val waste = wastagePercent.toIntOrNull() ?: 10

                    result = calculateTiles(
                        roomLengthM,
                        roomWidthM,
                        tileLengthM,
                        tileWidthM,
                        waste
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("Calculate", style = MaterialTheme.typography.titleMedium)
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
                    val roomArea =
                        (roomLength.toDoubleOrNull() ?: 0.0) * (roomWidth.toDoubleOrNull()
                            ?: 0.0)
                    if (roomArea > 0) {
                        Text(
                            text = "Room area: ${String.format("%.1f", roomArea)} sq ${measurementUnits.METERS.shortRep}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }


        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                showTileBottomSheet = !showTileBottomSheet
            },
            icon = { Icon(Icons.Filled.Edit, "Introduce new tile.") },
            text = { Text(text = "New Tile") },
        )
    }


    if (showTileBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showTileBottomSheet = false
            },
            sheetState = tileSheetState
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "New Tile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                scope.launch {
                                    tileSheetState.hide()
                                    showTileBottomSheet = false
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Close, "Close Tile Bottom Sheet.")
                        }
                    }

                    TileInputCard(
                        lengthValue = tileLength,
                        onLengthChange = { tileLength = it },
                        widthValue = tileWidth,
                        onWidthChange = { tileWidth = it },
                        lengthUnit = tileLengthUnit,
                        widthUnit = tileWidthUnit,
                        onLengthUnitChange = { tileLengthUnit = it },
                        onWidthUnitChange = { tileWidthUnit = it },
                    )

                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        OutlinedTextField(
                            value = wastagePercent,
                            onValueChange = { wastagePercent = it },
                            label = { Text("Waste (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = boxSize,
                            onValueChange = { boxSize = it },
                            label = { Text("Box Size") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }


                    Button(
                        onClick = {
                            if (tileLength.isEmpty() || tileWidth.isEmpty() || wastagePercent.isEmpty() || boxSize.isEmpty()) {
                                Toast.makeText(screenContext, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            scope.launch {
                                tileList = tileList + Tile(
                                    length = tileLength.toDouble(),
                                    width = tileWidth.toDouble(),
                                    lengthUnit = tileLengthUnit,
                                    widthUnit = tileWidthUnit,
                                    wastePercent = wastagePercent.toInt(),
                                    boxSize = boxSize.toInt()
                                )
                                tileSheetState.hide()
                            }.invokeOnCompletion {
                                if (!tileSheetState.isVisible) {
                                    showTileBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Tile")
                    }

                }
            }

        }
    }


    if (showRoomBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showRoomBottomSheet = false
            },
            sheetState = roomSheetState
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "New Room",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                scope.launch {
                                    roomSheetState.hide()
                                    showRoomBottomSheet = false
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Close, "Close Room Bottom Sheet.")
                        }
                    }

                    // Room Size Section - no presets
                    RoomInputCard(
                        roomName = roomName,
                        selectedTile = selectedTile,
                        onTileChange = { selectedTile = it },
                        onRoomNameChange = { roomName = it },
                        lengthValue = roomLength,
                        onLengthChange = { roomLength = it },
                        widthValue = roomWidth,
                        onWidthChange = { roomWidth = it },
                        tileList = tileList,
                        lengthUnit = roomLengthUnit,
                        widthUnit = roomWidthUnit,
                        onLengthUnitChange = { roomLengthUnit = it },
                        onWidthUnitChange = { roomWidthUnit = it },

                    )


                    Button(
                        onClick = {
                            if (roomLength.isEmpty() || roomWidth.isEmpty() || roomName.isEmpty() || selectedTile == null ) {
                                Toast.makeText(screenContext, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            scope.launch {
                                roomList = roomList + Room(
                                    name = roomName,
                                    length = roomLength.toDouble(),
                                    width = roomWidth.toDouble(),
                                    lengthUnit = roomLengthUnit,
                                    widthUnit = roomWidthUnit,
                                    tile = selectedTile!!
                                )
                                roomSheetState.hide()
                            }.invokeOnCompletion {
                                if (!roomSheetState.isVisible) {
                                    showRoomBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Room")
                    }

                }
            }

        }
    }

}