package com.aedo.aedoAdmin.model.login

data class UserModel(
    val phone : String? = null,
    val birth : String? = null,
    val name : String? = null,
    val admin : Boolean? = null,
    val terms : String? = null,
    val id : String? = null
)