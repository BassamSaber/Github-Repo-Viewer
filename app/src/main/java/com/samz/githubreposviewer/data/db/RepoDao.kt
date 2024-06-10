package com.samz.githubreposviewer.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samz.githubreposviewer.data.db.entity.RepoEntity

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query("SELECT * FROM repoEntity")
    fun pagingSource(): PagingSource<Int, RepoEntity>

    @Query("SELECT * FROM repoEntity WHERE id=:id")
    suspend fun getRepoDetails(id:Int):RepoEntity

    @Query("DELETE FROM repoEntity")
    suspend fun clearAll()
}