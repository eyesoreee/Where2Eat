package com.example.where2eat.db.repo

import com.example.where2eat.db.entity.OptionTagCrossRef

interface OptionTagCrossRefRepo {
    suspend fun insert(crossRef: OptionTagCrossRef)
    suspend fun delete(crossRef: OptionTagCrossRef)
    suspend fun deleteByOptionId(optionId: Long)
}