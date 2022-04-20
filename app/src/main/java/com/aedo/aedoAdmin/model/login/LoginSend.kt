package com.aedo.aedoAdmin.model.login

import com.google.gson.annotations.SerializedName

data class LoginSend(
    val phone : String? =null,
    val status : String ?= null,

    @SerializedName("Accesstoken")
    val accesstoken : String?=null
)