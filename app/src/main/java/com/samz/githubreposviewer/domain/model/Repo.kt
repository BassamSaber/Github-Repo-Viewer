package com.samz.githubreposviewer.domain.model

data class Repo(
    val id: Int,
    val name: String,
    val description: String?=null,
    val owner: String,
    val ownerAvatarUrl: String?,
    val fullRepoName: String,
    val isPrivate:Boolean
)
