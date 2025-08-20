package com.example.where2eat.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithOptions(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(OptionTagCrossRef::class)
    )
    val options: List<Option>
)
