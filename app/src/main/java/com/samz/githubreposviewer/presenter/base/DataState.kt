package com.samz.githubreposviewer.presenter.base

data class DataState<out T>(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null,
    val data: T? = null,
)