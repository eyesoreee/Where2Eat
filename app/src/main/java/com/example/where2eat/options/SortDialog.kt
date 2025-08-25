package com.example.where2eat.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SortDialog(
    currentSortType: SortType,
    onDismissRequest: () -> Unit,
    onSortTypeSelected: (SortType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Sort Options",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortType.entries.forEach { sortType ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSortTypeSelected(sortType) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (sortType == currentSortType)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (sortType == currentSortType) 2.dp else 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = sortType.displayName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (sortType == currentSortType)
                                            FontWeight.SemiBold
                                        else
                                            FontWeight.Normal
                                    ),
                                    color = if (sortType == currentSortType)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = getSortDescription(sortType),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (sortType == currentSortType) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                RadioButton(
                                    selected = false,
                                    onClick = { onSortTypeSelected(sortType) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Done")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

private fun getSortDescription(sortType: SortType): String {
    return when (sortType) {
        SortType.RECENT -> "Most recently added first"
        SortType.NAME -> "Alphabetical order"
        SortType.RATING -> "Highest rated first"
        SortType.LAST_CHOSEN -> "Recently chosen first"
        SortType.PRICE_LOW_TO_HIGH -> "Cheapest options first"
        SortType.PRICE_HIGH_TO_LOW -> "Most expensive first"
    }
}