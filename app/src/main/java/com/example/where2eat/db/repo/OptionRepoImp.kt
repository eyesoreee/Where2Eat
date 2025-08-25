package com.example.where2eat.db.repo

import com.example.where2eat.db.dao.OptionDao
import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.OptionWithTags

class OptionRepoImp(private val optionDao: OptionDao) : OptionRepo {
    override suspend fun insert(option: Option): Long {
        return optionDao.insert(option)
    }

    override suspend fun update(option: Option) {
        optionDao.update(option.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun delete(option: Option) {
        optionDao.delete(option)
    }

    override suspend fun getById(id: Long): Option? {
        return optionDao.getById(id)
    }

    override suspend fun getByIdWithTags(id: Long): OptionWithTags? {
        return optionDao.getByIdWithTags(id)
    }

    override suspend fun getOptions(filter: OptionFilter): List<Option> {
        return when (filter) {
            OptionFilter.Active -> optionDao.getActiveOptions()
            OptionFilter.All -> optionDao.getAll()
            OptionFilter.Archived -> optionDao.getArchivedOptions()
        }
    }

    override suspend fun getOptionsWithTags(filter: OptionFilter): List<OptionWithTags> {
        return when (filter) {
            OptionFilter.Active -> optionDao.getActiveOptionsWithTags()
            OptionFilter.All -> optionDao.getAllWithTags()
            OptionFilter.Archived -> optionDao.getArchivedOptionsWithTags()
        }
    }
}