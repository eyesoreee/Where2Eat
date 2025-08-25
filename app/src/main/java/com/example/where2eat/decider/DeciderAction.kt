package com.example.where2eat.decider

sealed interface DeciderAction {
    object ChooseOption : DeciderAction
}