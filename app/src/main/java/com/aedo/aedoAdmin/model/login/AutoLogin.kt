package com.aedo.aedoAdmin.model.login

import com.google.gson.annotations.SerializedName

data class AutoLogin(
    val status : String?=null,
    var id: String?=null,
    @SerializedName("Accesstoken")
    val accesstoken : String?=null
)
