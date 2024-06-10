package com.samz.githubreposviewer.data

import com.samz.githubreposviewer.data.api.model.IssueDto
import com.samz.githubreposviewer.data.api.model.RepoDto
import com.samz.githubreposviewer.data.db.entity.RepoEntity
import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.domain.model.Repo
import com.samz.githubreposviewer.domain.model.RepoDetails

fun RepoDto.toRepoEntity(): RepoEntity {
    return RepoEntity(
        id = id,
        name = name,
        description = description,
        owner = owner.login,
        ownerAvatarUrl = owner.avatar_url,
        fullRepoName = full_name,
        isPrivate = private?:false
//        starCount = stargazers_count,
//        watcherCount = watchers_count,
//        openedIssuesCount = open_issues_count,
//        visibility = visibility,
//        archived = archived,
//        allowForking = allow_forking,
//        forkingCount = forks_count
    )
}

fun RepoDto.toRepo(): Repo {
    return Repo(
        id = id,
        name = name,
        description = description,
        owner = owner.login,
        ownerAvatarUrl = owner.avatar_url,
        fullRepoName = full_name,
        isPrivate = private?:false
    )
}

fun RepoEntity.toRepo(): Repo {
    return Repo(
        id = id,
        name = name,
        description = description,
        owner = owner,
        ownerAvatarUrl = ownerAvatarUrl,
        fullRepoName = fullRepoName,
        isPrivate = isPrivate
    )
}

fun RepoDto.toRepoDetails(): RepoDetails {
    return RepoDetails(
        id = id,
        name = name,
        description = description,
        owner = owner.login,
        ownerAvatarUrl = owner.avatar_url,
        fullRepoName = full_name,
        isPrivate = private?:false,
        visibility = visibility,
        language = language,
        htmlUrl = html_url,
        forkingCount = forks_count,
        allowForking = allow_forking,
        openedIssuesCount = open_issues_count,
        starCount = stargazers_count,
        watcherCount = watchers_count
    )
}

fun IssueDto.toIssue(): Issue =
    Issue(
        id = id,
        title = title,
        userName = user.login,
        userImgUrl = user.avatar_url,
        state = state,
        createdDate = created_at,
        closedDate = closed_at
    )
