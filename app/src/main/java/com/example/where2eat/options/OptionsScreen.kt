package com.example.where2eat.options

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.where2eat.db.entity.OptionWithTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(viewModel: OptionsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAddOptionDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var showEditOptionDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<OptionWithTags?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterSearchCard(
                searchQuery = state.searchQuery,
                onSearchChange = { viewModel.onAction(OptionsAction.UpdateSearchQuery(it)) },
                onFilterClick = { showFilterDialog = true },
                onSortClick = { showSortDialog = true },
                onFavoritesClick = { viewModel.onAction(OptionsAction.ToggleFavoritesFilter) },
                onOpenClick = { viewModel.onAction(OptionsAction.ToggleOpenFilter) },
                hasActiveFilters = state.hasActiveFilters,
                currentSortType = state.sortType.displayName,
                favoritesActive = state.filterState.showFavoritesOnly,
                openActive = state.filterState.showOpenOnly
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${state.filteredOptions.size} Options",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            val favoritesCount =
                                state.filteredOptions.count { it.option.isFavorite }
                            if (favoritesCount > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = favoritesCount.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            val openCount = state.filteredOptions.count { it.option.isOpen }
                            if (openCount > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = openCount.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    ElevatedButton(
                        onClick = { showAddOptionDialog = true },
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                AnimatedContent(
                    targetState = state.filteredOptions.isEmpty(),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith
                                fadeOut(animationSpec = tween(300))
                    },
                    label = "options_content"
                ) { isEmpty ->
                    if (isEmpty) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.4f
                                                ),
                                                MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.1f
                                                )
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RestaurantMenu,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "No Options Found",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = if (state.searchQuery.isNotBlank() || state.hasActiveFilters) {
                                    "Try adjusting your search or filters"
                                } else {
                                    "Add your first dining option!"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                            )

                            OutlinedButton(
                                onClick = {
                                    if (state.searchQuery.isNotBlank() || state.hasActiveFilters) {
                                        viewModel.onAction(OptionsAction.ClearAllFilters)
                                    } else {
                                        showAddOptionDialog = true
                                    }
                                },
                                modifier = Modifier.height(48.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )
                                    Text(
                                        if (state.searchQuery.isNotBlank() || state.hasActiveFilters) {
                                            "Clear Filters"
                                        } else {
                                            "Add Option"
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.filteredOptions,
                                key = { it.option.id }
                            ) { optionWithTags ->
                                OptionCard(
                                    optionWithTags = optionWithTags,
                                    onClick = {
                                        selectedOption = optionWithTags
                                        showEditOptionDialog = true
                                    },
                                    onToggleFavorite = {
                                        viewModel.onAction(
                                            OptionsAction.ToggleFavorite(it.option)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        }
    }

    state.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(error)
        }
    }

    if (showAddOptionDialog) {
        AddOptionDialog(
            availableTags = state.availableTags,
            onDismissRequest = { showAddOptionDialog = false },
            onSave = { option, tags ->
                viewModel.onAction(OptionsAction.AddOptionWithTags(option, tags))
                showAddOptionDialog = false
            }
        )
    }

    if (showEditOptionDialog) {
        EditOptionDialog(
            option = selectedOption!!,
            availableTags = state.availableTags,
            onDismissRequest = { showEditOptionDialog = false },
            onSave = { optionWithTags ->
                viewModel.onAction(OptionsAction.UpdateOption(optionWithTags))
                showEditOptionDialog = false
                selectedOption = null
            }
        )
    }

    if (showFilterDialog) {
        FilterDialog(
            filterState = state.filterState,
            availableTags = state.availableTags,
            onDismissRequest = { showFilterDialog = false },
            onTagToggle = { tagId -> viewModel.onAction(OptionsAction.ToggleTagFilter(tagId)) },
            onPriceRangeToggle = { priceRange ->
                viewModel.onAction(
                    OptionsAction.TogglePriceRangeFilter(
                        priceRange
                    )
                )
            },
            onRatingFilterUpdate = { min, max ->
                viewModel.onAction(
                    OptionsAction.UpdateRatingFilter(
                        min,
                        max
                    )
                )
            },
            onClearFilters = { viewModel.onAction(OptionsAction.ClearAllFilters) }
        )
    }

    if (showSortDialog) {
        SortDialog(
            currentSortType = state.sortType,
            onDismissRequest = { showSortDialog = false },
            onSortTypeSelected = { sortType ->
                viewModel.onAction(OptionsAction.UpdateSortType(sortType))
                showSortDialog = false
            }
        )
    }
}