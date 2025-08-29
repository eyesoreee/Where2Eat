package com.example.where2eat.decider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.where2eat.db.repo.OptionFilter
import com.example.where2eat.db.repo.OptionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeciderViewModel @Inject constructor(
    private val optionRepository: OptionRepo
) : ViewModel() {
    private val _state = MutableStateFlow(DeciderState())
    val state = _state.asStateFlow()

    fun onAction(action: DeciderAction) {
        when (action) {
            DeciderAction.ChooseOption -> chooseOption()
        }
    }

    private fun chooseOption() {
        try {
            _state.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                val options = optionRepository.getOptionsWithTags(OptionFilter.Active)
                val randomOption = if (options.isNotEmpty()) options.random() else null
                delay(1500L)
                _state.update { it.copy(chosenOption = randomOption) }
                _state.update { it.copy(isLoading = false) }
            }

        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }
}