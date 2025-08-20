package com.example.where2eat.db.repo

import com.example.where2eat.db.entity.Tag
import com.example.where2eat.db.entity.TagWithOptions

interface TagRepo {
    suspend fun insert(tag: Tag): Long
    suspend fun update(tag: Tag)
    suspend fun delete(tag: Tag)
    suspend fun getAll(): List<Tag>
    suspend fun getAllWithOptions(): List<TagWithOptions>
    suspend fun getById(id: Long): Tag?
}