package com.example.where2eat.db.repo

import com.example.where2eat.db.dao.OptionTagCrossRefDao
import com.example.where2eat.db.entity.OptionTagCrossRef

class OptionTagCrossRefImp(
    private val optionTagCrossRefDao: OptionTagCrossRefDao
) : OptionTagCrossRefRepo {
    override suspend fun insert(crossRef: OptionTagCrossRef) {
        optionTagCrossRefDao.insertOptionTagCrossRef(crossRef)
    }

    override suspend fun delete(crossRef: OptionTagCrossRef) {
        optionTagCrossRefDao.deleteOptionTagCrossRef(crossRef)
    }

    override suspend fun deleteByOptionId(optionId: Long) {
        optionTagCrossRefDao.deleteOptionTagCrossRefsByOptionId(optionId)
    }
}