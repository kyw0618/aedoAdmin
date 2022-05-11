package com.aedo.aedoAdmin.model.notice

import com.google.gson.annotations.SerializedName

data class NoticeModel(
    @SerializedName("announcement")
    val result : List<Announcement>? = null
)

data class Announcement(
    val _id : String? = null,
    var title : String? = null,
    var content : String? = null,
    var created : String? = null,
    val id : String?= null
)