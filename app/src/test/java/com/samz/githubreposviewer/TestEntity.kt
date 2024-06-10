package com.samz.githubreposviewer

import com.samz.githubreposviewer.data.api.model.IssueDto
import com.samz.githubreposviewer.data.api.model.OwnerDto
import com.samz.githubreposviewer.data.api.model.RepoDto
import com.samz.githubreposviewer.data.api.model.User
import com.samz.githubreposviewer.data.toIssue
import com.samz.githubreposviewer.domain.model.Issue
import com.samz.githubreposviewer.domain.model.RepoDetails

val expectRepositoryDto = RepoDto(
    id = 1,
    name = "name",
    description = "description",
    owner = OwnerDto(id = 1, avatar_url = "avatar_url", login = "owner"),
    full_name = "full_name",
    private = false,
    visibility = "public",
    language = "C",
    html_url = "html_url",
    forks_count = 1,
    allow_forking = true,
    open_issues_count = 2,
    stargazers_count = 1,
    watchers_count = 2
)
val expectRepositoryDetailModel = RepoDetails(
    id = 1,
    name = "name",
    description = "description",
    owner = "owner",
    ownerAvatarUrl = "avatar_url",
    fullRepoName = "full_name",
    isPrivate = false,
    visibility = "public",
    language = "C",
    htmlUrl = "html_url",
    forkingCount = 1,
    allowForking = true,
    openedIssuesCount = 2,
    starCount = 1,
    watcherCount = 2
)

val expectRepositoryDetailModelList = listOf(expectRepositoryDetailModel)
val expectRepositoryDtoList = listOf(expectRepositoryDto)

val issueDto = IssueDto(
    id = 1L,
    title = "title",
    user = User(id = 1, avatar_url = "avater_url", login = "user"),
    state = "opened",
    created_at = "2024-04-11T10:11:22Z",
    closed_at = null
)
val issueModel = issueDto.toIssue()
val expectRepositoryIssuesDtoList = listOf(issueDto)
val expectRepositoryIssues = listOf(issueModel)
