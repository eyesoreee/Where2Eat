package com.example.where2eat.options

import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.OptionWithTags
import com.example.where2eat.db.entity.Tag

sealed interface OptionsAction {
    class AddOption(val option: Option) : OptionsAction
    class AddOptionWithTags(val option: Option, val tags: List<Tag>) : OptionsAction
    class DeleteOption(val option: Option) : OptionsAction
    class UpdateOption(val optionWithTags: OptionWithTags) : OptionsAction
    class ToggleFavorite(val option: Option) : OptionsAction

    // Search and Filter Actions
    class UpdateSearchQuery(val query: String) : OptionsAction
    class UpdateSortType(val sortType: SortType) : OptionsAction
    object ToggleFavoritesFilter : OptionsAction
    object ToggleOpenFilter : OptionsAction
    class ToggleTagFilter(val tagId: Int) : OptionsAction
    class UpdateRatingFilter(val minRating: Float?, val maxRating: Float?) : OptionsAction
    class TogglePriceRangeFilter(val priceRange: String) : OptionsAction
    object ClearAllFilters : OptionsAction
}