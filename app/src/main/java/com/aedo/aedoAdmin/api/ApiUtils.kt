package com.aedo.aedoAdmin.api

import com.aedo.aedoAdmin.util.`object`.Constant.BASE_URL
import com.aedo.aedoAdmin.util.`object`.Constant.BASE_URL_TEST

object ApiUtils {
    val apiService: APIService
    get() = RetrofitClient.getClient(BASE_URL).create(APIService::class.java)

}