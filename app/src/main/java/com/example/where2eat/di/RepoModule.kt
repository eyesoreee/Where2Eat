package com.example.where2eat.di

import com.example.where2eat.db.dao.OptionDao
import com.example.where2eat.db.dao.TagDao
import com.example.where2eat.db.repo.OptionRepo
import com.example.where2eat.db.repo.OptionRepoImp
import com.example.where2eat.db.repo.TagRepo
import com.example.where2eat.db.repo.TagRepoImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideOptionRepo(optionDao: OptionDao): OptionRepo {
        return OptionRepoImp(optionDao )
    }

    @Provides
    @Singleton
    fun provideTagRepo(tagDao: TagDao): TagRepo {
        return TagRepoImp(tagDao)
    }
}