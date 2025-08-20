package com.example.where2eat.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.where2eat.db.entity.Tag
import com.example.where2eat.db.entity.TagWithOptions

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Tag): Long

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getById(id: Long): Tag?

    @Query("SELECT * FROM tag")
    suspend fun getAll(): List<Tag>

    @Query("SELECT * FROM tag")
    suspend fun getAllWithOptions(): List<TagWithOptions>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(tag: Tag)

    @Delete
    suspend fun delete(tag: Tag)
}