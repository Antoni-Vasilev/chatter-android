package eu.nexanet.chatter_android.retrofit

import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.model.UserLoginRequest
import eu.nexanet.chatter_android.model.UserRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthController {

    @POST("/api/auth/sign-up")
    fun signUp(@Body userRegisterRequest: UserRegisterRequest): Call<MessageResponse>

    @POST("/api/auth/sign-in")
    fun signIn(@Body userLoginRequest: UserLoginRequest): Call<MessageResponse>

    @POST("/api/auth/renew")
    fun renew(
        @Query("sessionId") sessionId: String,
        @Query("deviceId") deviceId: String
    ): Call<MessageResponse>

    @POST("/api/auth/refresh")
    fun refresh(@Query("sessionId") sessionId: String): Call<MessageResponse>

    @POST("/api/auth/logout")
    fun logout(@Query("sessionId") sessionId: String): Call<MessageResponse>
}