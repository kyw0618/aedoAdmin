package com.aedo.aedoAdmin.model.list


data class RecyclerList(
    var result : List<Obituary>?=null,  //변수명 obituary 이면 응답 null이라고 뜸
)
data class Obituary(
    var id: String? = null,
    var imgName : String? = null,       //이미지 API 불러올 때 사용
    var resident : Resident? = null,
    var place : String? = null,
    var deceased : Deceased? = null,
    var eod : String? = null,
    var coffin : String? = null,
    var dofp : String? = null,
    var buried : String? = null,
    var word : String? = null
)

data class Resident(
    var relation : String? = null,
    var name : String? = null,
    var phone : String? = null
)

data class Deceased(
    var name: String? = null,
    var age: String? = null
)

