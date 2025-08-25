package com.example.where2eat.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.where2eat.db.entity.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagInputField(
    selectedTags: List<Tag>,
    availableTags: List<Tag>,
    onTagsChanged: (List<Tag>) -> Unit,
    modifier: Modifier = Modifier
) {
    var tagInput by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val filteredTags = remember(tagInput, selectedTags, availableTags) {
        availableTags.filter { tag ->
            tag.name.contains(tagInput, ignoreCase = true) &&
                    selectedTags.none { it.name.equals(tag.name, ignoreCase = true) }
        }
    }

    // Check if current input would create a new tag
    val canCreateNewTag = tagInput.trim().isNotEmpty() &&
            availableTags.none { it.name.equals(tagInput.trim(), ignoreCase = true) } &&
            selectedTags.none { it.name.equals(tagInput.trim(), ignoreCase = true) }

    Column(modifier = modifier) {
        // Selected tags display
        if (selectedTags.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(selectedTags) { tag ->
                    SelectedTagChip(
                        tag = tag,
                        onRemove = {
                            onTagsChanged(selectedTags - tag)
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded && (filteredTags.isNotEmpty() || canCreateNewTag),
            onExpandedChange = { isDropdownExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = tagInput,
                onValueChange = { input ->
                    tagInput = input
                    isDropdownExpanded = input.isNotEmpty()
                },
                label = { Text("Add tags") },
                placeholder = { Text("Type to search or create new tag") },
                trailingIcon = {
                    if (tagInput.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                tagInput = ""
                                isDropdownExpanded = false
                            }
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (canCreateNewTag) {
                            val newTag = Tag(name = tagInput.trim())
                            onTagsChanged(selectedTags + newTag)
                            tagInput = ""
                            isDropdownExpanded = false
                        }
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isDropdownExpanded && (filteredTags.isNotEmpty() || canCreateNewTag),
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                filteredTags.forEach { tag ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Label,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(tag.name)
                            }
                        },
                        onClick = {
                            onTagsChanged(selectedTags + tag)
                            tagInput = ""
                            isDropdownExpanded = false
                        }
                    )
                }

                if (canCreateNewTag) {
                    if (filteredTags.isNotEmpty()) {
                        HorizontalDivider()
                    }
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Create \"${tagInput.trim()}\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        },
                        onClick = {
                            val newTag = Tag(name = tagInput.trim())
                            onTagsChanged(selectedTags + newTag)
                            tagInput = ""
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedTagChip(
    tag: Tag,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = onRemove,
        label = { Text(tag.name) },
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove ${tag.name}",
                modifier = Modifier.size(16.dp)
            )
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            trailingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}