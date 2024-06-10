package com.samz.githubreposviewer.domain.model

data class Issue(
    val id: Long,
    val title: String,
    val userName: String,
    val userImgUrl: String?,
    val state: String,
    val createdDate:String,
    val closedDate:String?,
){
    val isClosed = !closedDate.isNullOrEmpty()
}
