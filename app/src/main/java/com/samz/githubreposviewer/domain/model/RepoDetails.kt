package com.samz.githubreposviewer.domain.model

data class RepoDetails(
    val id: Int,
    val name: String,
    val description: String? = null,
    val owner: String,
    val ownerAvatarUrl: String?,
    val fullRepoName: String,
    val isPrivate: Boolean,
    val starCount: Int? = null,
    val watcherCount: Int? = null,
    val openedIssuesCount: Int? = null,
    val visibility: String? = null,
    val archived: Boolean? = null,
    val allowForking: Boolean? = null,
    val forkingCount: Int? = null,
    val htmlUrl: String? = null,
    val language: String? =null
)
