package com.aedo.aedoAdmin.model.list

data class Condole (
    val condoleMessage : List<CondoleList>? = null
)

data class CondoleList(
    val title : String? = null,
    val content : String? = null,
    val created : String? = null,
    val obId : String? = null
)
