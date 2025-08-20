package com.example.where2eat.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.OptionWithTags

@Dao
interface OptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(option: Option): Long

    @Query("SELECT * FROM option WHERE id = :id")
    suspend fun getById(id: Long): Option?

    @Query("SELECT * FROM option")
    suspend fun getAll(): List<Option>

    @Query("SELECT * FROM option WHERE archived_at IS NULL")
    suspend fun getActiveOptions(): List<Option>

    @Query("SELECT * FROM option WHERE archived_at IS NOT NULL")
    suspend fun getArchivedOptions(): List<Option>

    @Query("SELECT * FROM option WHERE id = :id")
    suspend fun getByIdWithTags(id: Long): OptionWithTags?

    @Query("SELECT * FROM option")
    suspend fun getAllWithTags(): List<OptionWithTags>

    @Query("SELECT * FROM option WHERE archived_at IS NULL")
    suspend fun getActiveOptionsWithTags(): List<OptionWithTags>

    @Query("SELECT * FROM option WHERE archived_at IS NOT NULL")
    suspend fun getArchivedOptionsWithTags(): List<OptionWithTags>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(option: Option)

    @Delete
    suspend fun delete(option: Option)
}