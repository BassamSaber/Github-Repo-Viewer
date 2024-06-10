package com.samz.githubreposviewer.di

import android.content.Context
import androidx.room.Room
import com.samz.githubreposviewer.data.db.RepoDao
import com.samz.githubreposviewer.data.db.RepoDatabase
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
    fun provideBeerDatabase(@ApplicationContext context: Context): RepoDatabase {
        return Room.databaseBuilder(
            context,
            RepoDatabase::class.java,
            "repos.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepoDao(repoDatabase: RepoDatabase): RepoDao =
        repoDatabase.repDao

}