package com.example.where2eat.db.repo

import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.OptionWithTags

sealed class OptionFilter {
    object All : OptionFilter()
    object Active : OptionFilter()
    object Archived : OptionFilter()
}

interface OptionRepo {
    suspend fun insert(option: Option): Long
    suspend fun update(option: Option)
    suspend fun delete(option: Option)

    suspend fun getById(id: Long): Option?
    suspend fun getByIdWithTags(id: Long): OptionWithTags?
    suspend fun getOptions(filter: OptionFilter): List<Option>
    suspend fun getOptionsWithTags(filter: OptionFilter): List<OptionWithTags>
}