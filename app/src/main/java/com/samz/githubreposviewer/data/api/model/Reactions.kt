package com.samz.githubreposviewer.data.api.model

data class Reactions(
    val positive: Int,
    val negative: Int,
    val confused: Int,
    val eyes: Int,
    val heart: Int,
    val hooray: Int,
    val laugh: Int,
    val rocket: Int,
    val total_count: Int,
    val url: String
)