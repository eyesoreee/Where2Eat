package com.example.where2eat.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.OptionTagCrossRef
import com.example.where2eat.db.entity.OptionWithTags
import com.example.where2eat.db.entity.Tag
import com.example.where2eat.db.repo.OptionFilter
import com.example.where2eat.db.repo.OptionRepo
import com.example.where2eat.db.repo.OptionTagCrossRefRepo
import com.example.where2eat.db.repo.TagRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val optionRepo: OptionRepo,
    private val tagRepo: TagRepo,
    private val crossRefRepo: OptionTagCrossRefRepo,
) : ViewModel() {
    private val _state = MutableStateFlow(OptionsState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update {
                val allOptions = optionRepo.getOptionsWithTags(OptionFilter.Active)
                it.copy(
                    allOptions = allOptions,
                    filteredOptions = applyFiltersAndSorting(
                        allOptions,
                        it.searchQuery,
                        it.sortType,
                        it.filterState
                    ),
                    availableTags = tagRepo.getAll()
                )
            }
        }
    }

    fun onAction(action: OptionsAction) {
        when (action) {
            is OptionsAction.AddOption -> addOption(action.option)
            is OptionsAction.AddOptionWithTags -> addOptionWithTags(action.option, action.tags)
            is OptionsAction.DeleteOption -> deleteOption(action.option)
            is OptionsAction.UpdateOption -> updateOption(action.optionWithTags)
            is OptionsAction.ToggleFavorite -> toggleFavorite(action.option)

            // Filter and Search Actions
            is OptionsAction.UpdateSearchQuery -> updateSearchQuery(action.query)
            is OptionsAction.UpdateSortType -> updateSortType(action.sortType)
            is OptionsAction.ToggleFavoritesFilter -> toggleFavoritesFilter()
            is OptionsAction.ToggleOpenFilter -> toggleOpenFilter()
            is OptionsAction.ToggleTagFilter -> toggleTagFilter(action.tagId)
            is OptionsAction.UpdateRatingFilter -> updateRatingFilter(
                action.minRating,
                action.maxRating
            )

            is OptionsAction.TogglePriceRangeFilter -> togglePriceRangeFilter(action.priceRange)
            is OptionsAction.ClearAllFilters -> clearAllFilters()
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    query,
                    currentState.sortType,
                    currentState.filterState
                )
            )
        }
    }

    private fun updateSortType(sortType: SortType) {
        _state.update { currentState ->
            currentState.copy(
                sortType = sortType,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    currentState.searchQuery,
                    sortType,
                    currentState.filterState
                )
            )
        }
    }

    private fun toggleFavoritesFilter() {
        _state.update { currentState ->
            val newFilterState = currentState.filterState.copy(
                showFavoritesOnly = !currentState.filterState.showFavoritesOnly
            )
            currentState.copy(
                filterState = newFilterState,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    currentState.searchQuery,
                    currentState.sortType,
                    newFilterState
                )
            )
        }
    }

    private fun toggleOpenFilter() {
        _state.update { currentState ->
            val newFilterState = currentState.filterState.copy(
                showOpenOnly = !currentState.filterState.showOpenOnly
            )
            currentState.copy(
                filterState = newFilterState,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    currentState.searchQuery,
                    currentState.sortType,
                    newFilterState
                )
            )
        }
    }

    private fun toggleTagFilter(tagId: Int) {
        _state.update { currentState ->
            val selectedTags = currentState.filterState.selectedTags.toMutableSet()
            if (selectedTags.contains(tagId)) {
                selectedTags.remove(tagId)
            } else {
                selectedTags.add(tagId)
            }

            val newFilterState = currentState.filterState.copy(selectedTags = selectedTags)
            currentState.copy(
                filterState = newFilterState,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    currentState.searchQuery,
                    currentState.sortType,
                    newFilterState
                )
            )
        }
    }

    private fun updateRatingFilter(minRating: Float?, maxRating: Float?) {
        _state.update { currentState ->
            val newFilterState = currentState.filterState.copy(
                minRating = minRating,
                maxRating = maxRating
            )
            currentState.copy(
                filterState = newFilterState,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    currentState.searchQuery,
                    currentState.sortType,
                    newFilterState
                )
            )
        }
    }

    private fun togglePriceRangeFilter(priceRange: String) {
        _state.update { currentState ->
            val selectedRanges = currentState.filterState.selectedPriceRanges.toMutableSet()
            if (selectedRanges.contains(priceRange)) {
                selectedRanges.remove(priceRange)
            } else {
                selectedRanges.add(priceRange)
            }

            val newFilterState = currentState.filterState.copy(selectedPriceRanges = selectedRanges)
            currentState.copy(
                filterState = newFilterState,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    currentState.searchQuery,
                    currentState.sortType,
                    newFilterState
                )
            )
        }
    }

    private fun clearAllFilters() {
        _state.update { currentState ->
            val newFilterState = FilterState()
            currentState.copy(
                searchQuery = "",
                filterState = newFilterState,
                filteredOptions = applyFiltersAndSorting(
                    currentState.allOptions,
                    "",
                    currentState.sortType,
                    newFilterState
                )
            )
        }
    }

    private fun applyFiltersAndSorting(
        options: List<OptionWithTags>,
        searchQuery: String,
        sortType: SortType,
        filterState: FilterState
    ): List<OptionWithTags> {
        var filtered = options

        if (searchQuery.isNotBlank())
            filtered = filtered.filter { optionWithTags ->
                val option = optionWithTags.option
                option.name.contains(searchQuery, ignoreCase = true) ||
                        option.description?.contains(searchQuery, ignoreCase = true) == true ||
                        optionWithTags.tags.any { it.name.contains(searchQuery, ignoreCase = true) }
            }


        if (filterState.showFavoritesOnly)
            filtered = filtered.filter { it.option.isFavorite }

        if (filterState.showOpenOnly)
            filtered = filtered.filter { it.option.isOpen }

        if (filterState.selectedTags.isNotEmpty())
            filtered = filtered.filter { optionWithTags ->
                filterState.selectedTags.any { tagId ->
                    optionWithTags.tags.any { it.id == tagId }
                }
            }

        if (filterState.minRating != null || filterState.maxRating != null)
            filtered = filtered.filter { optionWithTags ->
                val rating = optionWithTags.option.rating ?: 0f
                (filterState.minRating == null || rating >= filterState.minRating) &&
                        (filterState.maxRating == null || rating <= filterState.maxRating)
            }

        if (filterState.selectedPriceRanges.isNotEmpty())
            filtered = filtered.filter { optionWithTags ->
                optionWithTags.option.priceRange?.symbol in filterState.selectedPriceRanges
            }

        return when (sortType) {
            SortType.RECENT -> filtered.sortedByDescending { it.option.createdAt }
            SortType.NAME -> filtered.sortedBy { it.option.name.lowercase() }
            SortType.RATING -> filtered.sortedByDescending { it.option.rating ?: 0f }
            SortType.LAST_CHOSEN -> filtered.sortedByDescending { it.option.lastChosenAt ?: 0L }
            SortType.PRICE_LOW_TO_HIGH -> filtered.sortedBy {
                it.option.priceRange?.ordinal ?: Int.MAX_VALUE
            }

            SortType.PRICE_HIGH_TO_LOW -> filtered.sortedByDescending {
                it.option.priceRange?.ordinal ?: -1
            }
        }
    }

    private fun addOptionWithTags(option: Option, tags: List<Tag>) {
        try {
            viewModelScope.launch {
                val optionId = optionRepo.insert(option)
                val processedTags = mutableListOf<Tag>()

                tags.forEach { tag ->
                    val existingTag = tagRepo.getAll().find {
                        it.name.equals(tag.name, ignoreCase = true)
                    }

                    if (existingTag != null) {
                        processedTags.add(existingTag)
                    } else {
                        val newTagId = tagRepo.insert(tag)
                        processedTags.add(tag.copy(id = newTagId.toInt()))
                    }
                }

                processedTags.forEach { tag ->
                    crossRefRepo.insert(
                        OptionTagCrossRef(
                            optionId = optionId.toInt(),
                            tagId = tag.id
                        )
                    )
                }

                loadData()
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private fun addOption(option: Option) {
        try {
            viewModelScope.launch {
                optionRepo.insert(option)
                loadData()
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private fun deleteOption(option: Option) {
        try {
            viewModelScope.launch {
                optionRepo.delete(option)
                loadData()
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private fun updateOption(optionWithTags: OptionWithTags) {
        try {
            viewModelScope.launch {
                crossRefRepo.deleteByOptionId(optionWithTags.option.id.toLong())

                optionRepo.update(optionWithTags.option)
                val processedTags = mutableListOf<Tag>()

                optionWithTags.tags.forEach { tag ->
                    val existingTag = tagRepo.getAll().find {
                        it.name.equals(tag.name, ignoreCase = true)
                    }

                    if (existingTag != null) {
                        processedTags.add(existingTag)
                    } else {
                        val newTagId = tagRepo.insert(tag)
                        processedTags.add(tag.copy(id = newTagId.toInt()))
                    }
                }

                processedTags.forEach { tag ->
                    crossRefRepo.insert(
                        OptionTagCrossRef(
                            optionId = optionWithTags.option.id,
                            tagId = tag.id
                        )
                    )
                }

                loadData()
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private fun toggleFavorite(option: Option) {
        try {
            viewModelScope.launch {
                optionRepo.update(option.copy(isFavorite = option.isFavorite.not()))
                loadData()
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }
}