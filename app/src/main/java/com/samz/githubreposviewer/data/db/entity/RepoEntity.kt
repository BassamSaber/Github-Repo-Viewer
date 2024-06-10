package com.samz.githubreposviewer.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repoEntity")
data class RepoEntity(
    @PrimaryKey
    val id:Int,
    val name: String,
    val description: String?,
    val owner: String,
    val ownerAvatarUrl: String?,
    val fullRepoName: String,
    val isPrivate:Boolean
//    val starCount: Int,
//    val watcherCount: Int,
//    val openedIssuesCount: Int,
//    val visibility: String,
//    val archived: Boolean,
//    val allowForking: Boolean,
//    val forkingCount: Int
)