package com.example.where2eat.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.PriceRange
import com.example.where2eat.db.entity.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionDialog(
    modifier: Modifier = Modifier,
    availableTags: List<Tag> = emptyList(),
    onDismissRequest: () -> Unit,
    onSave: (Option, List<Tag>) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf<List<Tag>>(emptyList()) }
    var ratingInput by remember { mutableStateOf("") }
    var selectedPriceRange by remember { mutableStateOf<PriceRange?>(null) }
    var isPriceRangeExpanded by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var isOpen by remember { mutableStateOf(true) }
    var nameTouched by remember { mutableStateOf(false) }

    val isNameValid = name.trim().isNotEmpty()
    val canSave = isNameValid

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Add option") },
        text = {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name *") },
                    isError = nameTouched && !isNameValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                if (nameTouched && !isNameValid) {
                    Text(
                        text = "Name is required",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp, max = 140.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )

                // Price Range Dropdown (unchanged)
                ExposedDropdownMenuBox(
                    expanded = isPriceRangeExpanded,
                    onExpandedChange = { isPriceRangeExpanded = !isPriceRangeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedPriceRange?.symbol ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Price Range (optional)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPriceRangeExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isPriceRangeExpanded,
                        onDismissRequest = { isPriceRangeExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                selectedPriceRange = null
                                isPriceRangeExpanded = false
                            }
                        )
                        PriceRange.entries.forEach { priceRange ->
                            DropdownMenuItem(
                                text = { Text("${priceRange.symbol} (${priceRange.name})") },
                                onClick = {
                                    selectedPriceRange = priceRange
                                    isPriceRangeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Rating Input (unchanged)
                OutlinedTextField(
                    value = ratingInput,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            ratingInput = input
                        }
                    },
                    label = { Text("Rating (optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. 4.5") }
                )

                // Replace the old tags input with the new TagInputField
                TagInputField(
                    selectedTags = selectedTags,
                    availableTags = availableTags,
                    onTagsChanged = { selectedTags = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // Switches (unchanged)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Favorite")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(checked = isFavorite, onCheckedChange = { isFavorite = it })
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Open")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(checked = isOpen, onCheckedChange = { isOpen = it })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    nameTouched = true
                    if (!canSave) return@TextButton
                    focusManager.clearFocus()

                    val createdAt = System.currentTimeMillis()
                    val option = Option(
                        name = name.trim(),
                        description = description.trim().ifEmpty { null },
                        priceRange = selectedPriceRange,
                        rating = ratingInput.toFloatOrNull(),
                        isFavorite = isFavorite,
                        isOpen = isOpen,
                        lastChosenAt = null,
                        createdAt = createdAt,
                        updatedAt = createdAt,
                        archivedAt = null
                    )

                    onSave(option, selectedTags)
                },
                enabled = canSave
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}