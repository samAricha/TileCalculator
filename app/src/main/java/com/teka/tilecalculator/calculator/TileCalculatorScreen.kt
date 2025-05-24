package com.teka.tilecalculator.calculator

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.House
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TileCalculatorScreen() {
    val scrollState = rememberScrollState()
    val screenContext = LocalContext.current

    var roomName by remember { mutableStateOf("") }
    var roomLength by remember { mutableStateOf("") }
    var roomWidth by remember { mutableStateOf("") }
    var roomLengthUnit by remember { mutableStateOf(MeasurementUnits.METERS) }
    var roomWidthUnit by remember { mutableStateOf(MeasurementUnits.METERS) }

    var tileLength by remember { mutableStateOf("") }
    var tileWidth by remember { mutableStateOf("") }
    var tileLengthUnit by remember { mutableStateOf(MeasurementUnits.INCHES) }
    var tileWidthUnit by remember { mutableStateOf(MeasurementUnits.INCHES) }

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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Tile Calculator",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.primary
            )


            LazyRow(horizontalArrangement = Arrangement.spacedBy(1.5.dp)) {
                items(tileList) { tile ->
                    TileBox(
                        length = tile.length,
                        width = tile.width,
                        lengthUnit = tile.lengthUnit,
                        widthUnit = tile.widthUnit,
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                stickyHeader {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        val buttonGradient = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                            )
                        )


                        Button(
                            onClick = {
                                showRoomBottomSheet = !showRoomBottomSheet
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.White.copy(alpha = 0.7f)
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = buttonGradient,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.House,
                                        contentDescription = "New Room",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "New Room",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }


                items(roomList) { room ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val roomLengthM =
                                convertToMeters(room.length, room.lengthUnit)
                            val roomWidthM =
                                convertToMeters(room.width, room.widthUnit)
                            val tileLengthM =
                                convertToMeters(room.tile.length, room.tile.lengthUnit)
                            val tileWidthM =
                                convertToMeters(room.tile.width, room.tile.widthUnit)
                            val waste = room.tile.wastePercent

                            val tileBoxCount = calculateTiles(
                                roomLengthM,
                                roomWidthM,
                                tileLengthM,
                                tileWidthM,
                                room.tile.boxSize,
                                waste
                            )

                            val roomArea = (room.length) * (room.width)


                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = room.name.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Tile (${room.tile.length} ${room.tile.lengthUnit.shortRep} x ${room.tile.width} ${room.tile.widthUnit.shortRep})",
                                    fontWeight = FontWeight.ExtraLight
                                )
                            }

                            Text(
                                text = "Length: ${room.length} ${room.lengthUnit.shortRep}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.8f
                                )
                            )

                            Text(
                                text = "Width: ${room.width}  ${room.widthUnit.shortRep}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.8f
                                )
                            )




                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Boxes: ${tileBoxCount.boxCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                        alpha = 0.8f
                                    ),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Area: ${String.format("%.1f", roomArea) } sq ${MeasurementUnits.METERS.shortRep}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                        alpha = 0.8f
                                    ),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Tiles: ${tileBoxCount.tileCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                        alpha = 0.8f
                                    ),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
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
                            if (selectedTile == null) {
                                selectedTile = initTileList.first()
                            }

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