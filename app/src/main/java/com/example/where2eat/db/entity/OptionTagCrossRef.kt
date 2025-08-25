package com.example.where2eat.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "option_tag_cross_ref",
    primaryKeys = ["option_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Option::class,
            parentColumns = ["id"],
            childColumns = ["option_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["option_id"]),
        Index(value = ["tag_id"])
    ]
)
data class OptionTagCrossRef(
    @ColumnInfo(name = "option_id")
    val optionId: Int,
    @ColumnInfo(name = "tag_id")
    val tagId: Int
)
