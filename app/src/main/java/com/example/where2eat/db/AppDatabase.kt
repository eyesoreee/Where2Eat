package com.example.where2eat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.where2eat.db.dao.OptionDao
import com.example.where2eat.db.dao.TagDao
import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.Tag

@Database(
    entities = [Option::class, Tag::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun optionDao(): OptionDao
    abstract fun tagDao(): TagDao
}