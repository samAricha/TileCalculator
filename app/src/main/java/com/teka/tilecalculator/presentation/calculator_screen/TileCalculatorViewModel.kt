package com.teka.tilecalculator.presentation.calculator_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teka.tilecalculator.data.repository.TileRoomsRepository
import com.teka.tilecalculator.data.repository.TilesRepository
import com.teka.tilecalculator.presentation.calculator_screen.components.MeasurementUnits
import com.teka.tilecalculator.presentation.calculator_screen.components.RoomWithTile
import com.teka.tilecalculator.presentation.calculator_screen.components.Tile
import com.teka.tilecalculator.presentation.calculator_screen.components.TileRoom
import com.teka.tilecalculator.presentation.calculator_screen.components.calculateTiles
import com.teka.tilecalculator.presentation.calculator_screen.components.convertToMeters
import com.teka.tilecalculator.presentation.calculator_screen.components.initTileList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TileCalculatorUiState(
    val roomName: String = "",
    val roomLength: String = "",
    val roomWidth: String = "",
    val roomLengthUnit: MeasurementUnits = MeasurementUnits.METERS,
    val roomWidthUnit: MeasurementUnits = MeasurementUnits.METERS,

    val tileLength: String = "",
    val tileWidth: String = "",
    val tileLengthUnit: MeasurementUnits = MeasurementUnits.INCHES,
    val tileWidthUnit: MeasurementUnits = MeasurementUnits.INCHES,

    val selectedTile: Tile? = null,
    val tileList: List<Tile> = emptyList<Tile>(),
    val tileRoomList: List<RoomWithTile> = emptyList(),
    val wastagePercent: String = "10",
    val boxSize: String = "",

    val showTileBottomSheet: Boolean = false,
    val showRoomBottomSheet: Boolean = false
)

class TileCalculatorViewModel(
    private val tilesRepository: TilesRepository,
    private val tileRoomsRepository: TileRoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TileCalculatorUiState())
    val uiState: StateFlow<TileCalculatorUiState> = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            tilesRepository.initializeIfNeeded()
        }
        fetchTiles()
        fetchRoomsWithTiles()
    }

    private fun fetchTiles() {
        viewModelScope.launch {
            tilesRepository.getAll().collect { tiles ->
                _uiState.value = _uiState.value.copy(
                    tileList = tiles,
                    selectedTile = tiles.firstOrNull()
                )
            }
        }
    }

    private fun fetchRoomsWithTiles() {
        viewModelScope.launch {
            tileRoomsRepository.getAllRoomsWithTilesStream().collect { tileRooms ->
                _uiState.value = _uiState.value.copy(tileRoomList = tileRooms)
            }
        }
    }





    // Room-related methods
    fun updateRoomName(name: String) {
        _uiState.value = _uiState.value.copy(roomName = name)
    }

    fun updateRoomLength(length: String) {
        _uiState.value = _uiState.value.copy(roomLength = length)
    }

    fun updateRoomWidth(width: String) {
        _uiState.value = _uiState.value.copy(roomWidth = width)
    }

    fun updateRoomLengthUnit(unit: MeasurementUnits) {
        _uiState.value = _uiState.value.copy(roomLengthUnit = unit)
    }

    fun updateRoomWidthUnit(unit: MeasurementUnits) {
        _uiState.value = _uiState.value.copy(roomWidthUnit = unit)
    }

    // Tile-related methods
    fun updateTileLength(length: String) {
        _uiState.value = _uiState.value.copy(tileLength = length)
    }

    fun updateTileWidth(width: String) {
        _uiState.value = _uiState.value.copy(tileWidth = width)
    }

    fun updateTileLengthUnit(unit: MeasurementUnits) {
        _uiState.value = _uiState.value.copy(tileLengthUnit = unit)
    }

    fun updateTileWidthUnit(unit: MeasurementUnits) {
        _uiState.value = _uiState.value.copy(tileWidthUnit = unit)
    }

    fun updateSelectedTile(tile: Tile?) {
        Log.i("SelectedTile", "$tile")
        _uiState.value = _uiState.value.copy(selectedTile = tile)
    }

    fun updateWastagePercent(wastage: String) {
        _uiState.value = _uiState.value.copy(wastagePercent = wastage)
    }

    fun updateBoxSize(size: String) {
        _uiState.value = _uiState.value.copy(boxSize = size)
    }

    // Bottom sheet methods
    fun showTileBottomSheet() {
        _uiState.value = _uiState.value.copy(showTileBottomSheet = true)
    }

    fun hideTileBottomSheet() {
        _uiState.value = _uiState.value.copy(showTileBottomSheet = false)
    }

    fun showRoomBottomSheet() {
        _uiState.value = _uiState.value.copy(showRoomBottomSheet = true)
    }

    fun hideRoomBottomSheet() {
        _uiState.value = _uiState.value.copy(showRoomBottomSheet = false)
    }



    fun addTile(): ValidationResult {
        val currentState = _uiState.value

        if (currentState.tileLength.isEmpty() ||
            currentState.tileWidth.isEmpty() ||
            currentState.wastagePercent.isEmpty() ||
            currentState.boxSize.isEmpty()) {
            return ValidationResult.Error("Please fill in all fields.")
        }

        try {
            val newTile = Tile(
                length = currentState.tileLength.toDouble(),
                width = currentState.tileWidth.toDouble(),
                lengthUnit = currentState.tileLengthUnit,
                widthUnit = currentState.tileWidthUnit,
                wastePercent = currentState.wastagePercent.toInt(),
                boxSize = currentState.boxSize.toInt()
            )

            viewModelScope.launch {
                tilesRepository.insert(newTile)
            }

            // Only clear the form fields - Room will automatically update tileList via fetchTiles()
            _uiState.value = currentState.copy(
                tileLength = "",
                tileWidth = "",
                wastagePercent = "10",
                boxSize = ""
            )

            return ValidationResult.Success
        } catch (e: NumberFormatException) {
            return ValidationResult.Error("Please enter valid numbers.")
        }
    }

    fun addRoom(): ValidationResult {
        val currentState = _uiState.value
        val selectedTile = currentState.selectedTile
        Log.i("AddRoom", "${selectedTile}")


        if (currentState.roomLength.isEmpty() ||
            currentState.roomWidth.isEmpty() ||
            currentState.roomName.isEmpty() ||
            selectedTile == null) {
            return ValidationResult.Error("Please fill in all fields.")
        }

        try {
            val newRoom = TileRoom(
                name = currentState.roomName,
                length = currentState.roomLength.toDouble(),
                width = currentState.roomWidth.toDouble(),
                lengthUnit = currentState.roomLengthUnit,
                widthUnit = currentState.roomWidthUnit,
                tileId = selectedTile.id
            )

            viewModelScope.launch {
                tileRoomsRepository.insert(newRoom)
            }

            _uiState.value = currentState.copy(
                roomName = "",
                roomLength = "",
                roomWidth = "",
                selectedTile = null
            )

            return ValidationResult.Success
        } catch (e: NumberFormatException) {
            return ValidationResult.Error("Please enter valid numbers.")
        }
    }

    fun calculateTileInfo(roomWithTile: RoomWithTile): TileCalculationResult {
        val roomLengthM = convertToMeters(roomWithTile.room.length, roomWithTile.room.lengthUnit)
        val roomWidthM = convertToMeters(roomWithTile.room.width, roomWithTile.room.widthUnit)
        val tileLengthM = convertToMeters(roomWithTile.tile.length, roomWithTile.tile.lengthUnit)
        val tileWidthM = convertToMeters(roomWithTile.tile.width, roomWithTile.tile.widthUnit)
        val waste = roomWithTile.tile.wastePercent

        val tileBoxCount = calculateTiles(
            roomLengthM,
            roomWidthM,
            tileLengthM,
            tileWidthM,
            roomWithTile.tile.boxSize,
            waste
        )

        val roomArea = roomWithTile.room.length * roomWithTile.room.width

        return TileCalculationResult(
            boxCount = tileBoxCount.boxCount,
            tileCount = tileBoxCount.tileCount,
            roomArea = roomArea
        )
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

data class TileCalculationResult(
    val boxCount: Int,
    val tileCount: Int,
    val roomArea: Double
)