package com.teka.tilecalculator.calculator

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TileCalculatorScreen(
    viewModel: TileCalculatorViewModel = viewModel()
) {
    val screenContext = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()
    val tileSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val roomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
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
                items(uiState.tileList) { tile ->
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
                            onClick = { viewModel.showRoomBottomSheet() },
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

                items(uiState.tileRoomList) { room ->
                    val calculationResult = viewModel.calculateTileInfo(room)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = room.name.replaceFirstChar { it.uppercase() },
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Tile (${room.tile.length} ${room.tile.lengthUnit.shortRep} x ${room.tile.width} ${room.tile.widthUnit.shortRep})",
                                    fontWeight = FontWeight.ExtraLight
                                )
                            }

                            Text(
                                text = "Length: ${room.length} ${room.lengthUnit.shortRep}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )

                            Text(
                                text = "Width: ${room.width} ${room.widthUnit.shortRep}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Boxes: ${calculationResult.boxCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Area: ${String.format("%.1f", calculationResult.roomArea)} sq ${MeasurementUnits.METERS.shortRep}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Tiles: ${calculationResult.tileCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
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
            onClick = { viewModel.showTileBottomSheet() },
            icon = { Icon(Icons.Filled.Edit, "Introduce new tile.") },
            text = { Text(text = "New Tile") },
        )
    }

    // Tile Bottom Sheet
    if (uiState.showTileBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideTileBottomSheet() },
            sheetState = tileSheetState
        ) {
            TileBottomSheetContent(
                uiState = uiState,
                viewModel = viewModel,
                onSave = {
                    val result = viewModel.addTile()
                    when (result) {
                        is ValidationResult.Success -> {
                            scope.launch {
                                tileSheetState.hide()
                            }.invokeOnCompletion {
                                if (!tileSheetState.isVisible) {
                                    viewModel.hideTileBottomSheet()
                                }
                            }
                        }
                        is ValidationResult.Error -> {
                            Toast.makeText(screenContext, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onClose = {
                    scope.launch {
                        tileSheetState.hide()
                        viewModel.hideTileBottomSheet()
                    }
                }
            )
        }
    }

    // Room Bottom Sheet
    if (uiState.showRoomBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideRoomBottomSheet() },
            sheetState = roomSheetState
        ) {
            RoomBottomSheetContent(
                uiState = uiState,
                viewModel = viewModel,
                onSave = {
                    val result = viewModel.addRoom()
                    when (result) {
                        is ValidationResult.Success -> {
                            scope.launch {
                                roomSheetState.hide()
                            }.invokeOnCompletion {
                                if (!roomSheetState.isVisible) {
                                    viewModel.hideRoomBottomSheet()
                                }
                            }
                        }
                        is ValidationResult.Error -> {
                            Toast.makeText(screenContext, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onClose = {
                    scope.launch {
                        roomSheetState.hide()
                        viewModel.hideRoomBottomSheet()
                    }
                }
            )
        }
    }
}

@Composable
private fun TileBottomSheetContent(
    uiState: TileCalculatorUiState,
    viewModel: TileCalculatorViewModel,
    onSave: () -> Unit,
    onClose: () -> Unit
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

                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, "Close Tile Bottom Sheet.")
                }
            }

            TileInputCard(
                lengthValue = uiState.tileLength,
                onLengthChange = viewModel::updateTileLength,
                widthValue = uiState.tileWidth,
                onWidthChange = viewModel::updateTileWidth,
                lengthUnit = uiState.tileLengthUnit,
                widthUnit = uiState.tileWidthUnit,
                onLengthUnitChange = viewModel::updateTileLengthUnit,
                onWidthUnitChange = viewModel::updateTileWidthUnit,
            )

            Row(
                modifier = Modifier.padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                OutlinedTextField(
                    value = uiState.wastagePercent,
                    onValueChange = viewModel::updateWastagePercent,
                    label = { Text("Waste (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.boxSize,
                    onValueChange = viewModel::updateBoxSize,
                    label = { Text("Box Size") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Tile")
            }
        }
    }
}

@Composable
private fun RoomBottomSheetContent(
    uiState: TileCalculatorUiState,
    viewModel: TileCalculatorViewModel,
    onSave: () -> Unit,
    onClose: () -> Unit
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

                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, "Close Room Bottom Sheet.")
                }
            }

            RoomInputCard(
                roomName = uiState.roomName,
                selectedTile = uiState.selectedTile,
                onTileChange = viewModel::updateSelectedTile,
                onRoomNameChange = viewModel::updateRoomName,
                lengthValue = uiState.roomLength,
                onLengthChange = viewModel::updateRoomLength,
                widthValue = uiState.roomWidth,
                onWidthChange = viewModel::updateRoomWidth,
                tileList = uiState.tileList,
                lengthUnit = uiState.roomLengthUnit,
                widthUnit = uiState.roomWidthUnit,
                onLengthUnitChange = viewModel::updateRoomLengthUnit,
                onWidthUnitChange = viewModel::updateRoomWidthUnit,
            )

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Room")
            }
        }
    }
}