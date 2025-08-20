package com.example.where2eat.db.repo

import com.example.where2eat.db.dao.TagDao
import com.example.where2eat.db.entity.Tag
import com.example.where2eat.db.entity.TagWithOptions

class TagRepoImp(private val tagDao: TagDao) : TagRepo {
    override suspend fun insert(tag: Tag): Long {
        return tagDao.insert(tag)
    }

    override suspend fun update(tag: Tag) {
        tagDao.update(tag)
    }

    override suspend fun delete(tag: Tag) {
        tagDao.delete(tag)
    }

    override suspend fun getAll(): List<Tag> {
        return tagDao.getAll()
    }

    override suspend fun getAllWithOptions(): List<TagWithOptions> {
        return tagDao.getAllWithOptions()
    }

    override suspend fun getById(id: Long): Tag? {
        return tagDao.getById(id)
    }

}