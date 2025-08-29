package com.example.where2eat.options

import com.example.where2eat.db.entity.OptionWithTags
import com.example.where2eat.db.entity.Tag

enum class SortType(val displayName: String) {
    RECENT("Recent"),
    NAME("Name"),
    RATING("Rating"),
    LAST_CHOSEN("Last Chosen"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low")
}

data class FilterState(
    val selectedTags: Set<Int> = emptySet(),
    val showFavoritesOnly: Boolean = false,
    val showOpenOnly: Boolean = false,
    val showArchived: Boolean = false,
    val minRating: Float? = null,
    val maxRating: Float? = null,
    val selectedPriceRanges: Set<String> = emptySet()
)

data class OptionsState(
    val allOptions: List<OptionWithTags> = emptyList(),
    val filteredOptions: List<OptionWithTags> = emptyList(),
    val availableTags: List<Tag> = emptyList(),
    val searchQuery: String = "",
    val sortType: SortType = SortType.RECENT,
    val filterState: FilterState = FilterState(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val hasActiveFilters: Boolean
        get() = filterState.selectedTags.isNotEmpty() ||
                filterState.showFavoritesOnly ||
                filterState.showOpenOnly ||
                filterState.minRating != null ||
                filterState.maxRating != null ||
                filterState.selectedPriceRanges.isNotEmpty()
}