package com.example.where2eat.decider

import com.example.where2eat.db.entity.OptionWithTags

data class DeciderState(
    val chosenOption: OptionWithTags? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
