package com.example.where2eat.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithOptions(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = OptionTagCrossRef::class,
            parentColumn = "tag_id",
            entityColumn = "option_id"
        )
    )
    val options: List<Option>
)