package com.example.where2eat.di

import android.content.Context
import androidx.room.Room
import com.example.where2eat.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "where2eat.db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    @Singleton
    fun provideOptionDao(database: AppDatabase) = database.optionDao()

    @Provides
    @Singleton
    fun provideTagDao(database: AppDatabase) = database.tagDao()

    @Provides
    @Singleton
    fun provideOptionTagCrossRefDao(database: AppDatabase) = database.optionTagCrossRefDao()
}