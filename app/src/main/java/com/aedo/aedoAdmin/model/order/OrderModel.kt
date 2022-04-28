package com.aedo.aedoAdmin.model.order

data class OrderModel (
    val result : List<GetOrder>? = null
)

data class GetOrder(
    val place : MyPlace? = null,
    val item : String? = null,
    val price : String? = null,
    val receiver : MyReceiver? = null,
    val sender : MySender? = null,
    val word : MyWord? = null,
    val created : String? = null,
    val id : String? = null
)

data class MyPlace(
    val name : String? = null,
    val number : String? = null
)

data class MyReceiver(
    val name : String? = null,
    val phone : String? = null
)

data class MySender(
    val name : String? = null,
    val phone : String? = null,
)

data class MyWord(
    val company : String? = null,
    val word : String? = null,
    val wordsecond : String? = null
)