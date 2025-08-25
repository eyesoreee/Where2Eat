package com.example.where2eat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.where2eat.db.dao.OptionDao
import com.example.where2eat.db.dao.OptionTagCrossRefDao
import com.example.where2eat.db.dao.TagDao
import com.example.where2eat.db.entity.Option
import com.example.where2eat.db.entity.OptionTagCrossRef
import com.example.where2eat.db.entity.Tag

@Database(
    entities = [Option::class, Tag::class, OptionTagCrossRef::class],
    version = 5,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun optionDao(): OptionDao
    abstract fun tagDao(): TagDao
    abstract fun optionTagCrossRefDao(): OptionTagCrossRefDao
}