package com.example.where2eat.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class OptionWithTags(
    @Embedded val option: Option,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = OptionTagCrossRef::class,
            parentColumn = "option_id",
            entityColumn = "tag_id"
        )
    )
    val tags: List<Tag>
)
