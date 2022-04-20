package com.aedo.aedoAdmin.api

import com.aedo.aedoAdmin.model.intro.AppPolicy
import com.aedo.aedoAdmin.model.intro.Verification
import com.aedo.aedoAdmin.model.login.AutoLogin
import com.aedo.aedoAdmin.model.login.LoginSend
import com.aedo.aedoAdmin.model.login.UserModel
import retrofit2.Call
import retrofit2.http.*


interface APIService {
    // 검증 API
    @GET("v1/app/verification")
    fun getVerification(@Header("abcd-ef")abcdef: String?): Call<Verification>

    // 정책 API
    @GET("v1/app/policy")
    fun getPolicy(): Call<AppPolicy>

    // 자동로그인 API
    @PUT("v1/user/auto")
    fun getautoLogin(@Header("Accesstoken")accesstoken: String?): Call<AutoLogin>

    // 로그인 API
    @PUT("v1/user")
    fun getLogin(@Body loginSend: LoginSend):Call<LoginSend>

    //회원정보
    @GET("v1/user")
    fun getUser(@Header("Accesstoken")accesstoken: String?): Call<UserModel>
}