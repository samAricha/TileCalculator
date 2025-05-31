package com.teka.tilecalculator.core

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.teka.tilecalculator.presentation.calculator_screen.TileCalculatorViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TileCalculatorViewModel(
                tileCalculatorApplication().container.tilesRepository,
                tileCalculatorApplication().container.tileRoomsRepository
            )
        }

    }
}


fun CreationExtras.tileCalculatorApplication(): TileApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TileApplication)
