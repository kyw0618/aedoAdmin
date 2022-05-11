package com.aedo.aedoAdmin.api

import com.aedo.aedoAdmin.model.intro.AppPolicy
import com.aedo.aedoAdmin.model.intro.Verification
import com.aedo.aedoAdmin.model.list.Condole
import com.aedo.aedoAdmin.model.list.RecyclerList
import com.aedo.aedoAdmin.model.login.AutoLogin
import com.aedo.aedoAdmin.model.login.LoginSMS
import com.aedo.aedoAdmin.model.login.LoginSend
import com.aedo.aedoAdmin.model.login.UserModel
import com.aedo.aedoAdmin.model.order.OrderModel
import okhttp3.ResponseBody
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

    // 문자인증 API
    @POST("v1/user/sms")
    fun getSMS(@Body loginSMS: LoginSMS): Call<LoginSMS>

    //회원정보
    @GET("v1/user/admin")
    fun getUser(@Header("Accesstoken")accesstoken: String?): Call<UserModel>

    //주문정보
    @GET("v1/order")
    fun getOrder(@Header("Accesstoken")accesstoken: String?): Call<OrderModel>

    // 부고조회
    @GET("v1/obituary")
    fun getObituary(@Query("name")name: String?,
                      @Header("Accesstoken")accesstoken: String?) : Call<RecyclerList>

    //부고 이미지 받기
    @Streaming
    @GET("v1/obituary/image")
    fun getImg(@Query("imgname")imgname: String?,
               @Header("Accesstoken")accesstoken: String?) : Call<ResponseBody>

    // 조문메세지 조회
    @GET("v1/condole")
    fun getConID(@Query("id")id: String? ,
                 @Header("Accesstoken")accesstoken: String?) : Call<Condole>

}