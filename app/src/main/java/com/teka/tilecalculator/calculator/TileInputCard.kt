package com.teka.tilecalculator.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TileInputCard(
    lengthValue: String,
    onLengthChange: (String) -> Unit,
    widthValue: String,
    onWidthChange: (String) -> Unit,
    lengthUnit: measurementUnits,
    widthUnit: measurementUnits,
    onLengthUnitChange: (measurementUnits) -> Unit,
    onWidthUnitChange: (measurementUnits) -> Unit,
) {
    var lengthUnitExpanded by remember { mutableStateOf(false) }
    var widthUnitExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(.6f),
                    value = lengthValue,
                    onValueChange = onLengthChange,
                    label = { Text("Length (${lengthUnit.shortRep})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(.4f),
                    expanded = lengthUnitExpanded,
                    onExpandedChange = { lengthUnitExpanded = !lengthUnitExpanded }
                ) {
                    FilterChip(
                        onClick = { lengthUnitExpanded = true },
                        label = {
                            Text(
                                lengthUnit.unitName,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        selected = false,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                lengthUnitExpanded
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = lengthUnitExpanded,
                        onDismissRequest = { lengthUnitExpanded = false }
                    ) {
                        measurementUnits.entries.forEach { unitOption ->
                            DropdownMenuItem(
                                text = { Text(unitOption.unitName) },
                                onClick = {
                                    onLengthUnitChange(unitOption)
                                    lengthUnitExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(.6f),
                    value = widthValue,
                    onValueChange = onWidthChange,
                    label = { Text("Width (${widthUnit.shortRep})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(.4f),
                    expanded = widthUnitExpanded,
                    onExpandedChange = { widthUnitExpanded = !widthUnitExpanded }
                ) {
                    FilterChip(
                        onClick = { widthUnitExpanded = true },
                        label = {
                            Text(
                                widthUnit.unitName,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        selected = false,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                widthUnitExpanded
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = widthUnitExpanded,
                        onDismissRequest = { widthUnitExpanded = false }
                    ) {
                        measurementUnits.entries.forEach { unitOption ->
                            DropdownMenuItem(
                                text = { Text(unitOption.unitName) },
                                onClick = {
                                    onWidthUnitChange(unitOption)
                                    widthUnitExpanded = false
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}
