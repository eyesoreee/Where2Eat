package com.example.where2eat.di

import android.content.Context
import androidx.room.Room
import com.example.where2eat.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn( SingletonComponent::class)
object DatabaseModule {

     @Provides
     fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
         return Room.databaseBuilder(
             appContext,
             AppDatabase::class.java,
             "where2eat.db"
         ).build()
     }
}