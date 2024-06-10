package com.samz.githubreposviewer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.samz.githubreposviewer.data.db.entity.RepoEntity

@Database(
    entities = [RepoEntity::class],
    version = 1
)
abstract class RepoDatabase : RoomDatabase() {
    abstract val repDao: RepoDao
}