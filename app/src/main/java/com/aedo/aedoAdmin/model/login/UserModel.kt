package com.aedo.aedoAdmin.model.login

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("result")
    var getuser : List<GetUser>?=null
)

data class GetUser (
    val phone : String? = null,
    val birth : String? = null,
    val name : String? = null,
    val admin : Boolean? = null,
    val terms : String? = null,
    val id : String? = null
)