package com.example.where2eat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    DECIDER(
        "decider",
        "Decider",
        Icons.Default.Shuffle,
        "Responsible for choosing the place/restaurant/eateries"
    ),
    OPTIONS("options", "Options", Icons.AutoMirrored.Filled.List, "List of options"),
}