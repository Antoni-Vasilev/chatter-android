package eu.nexanet.chatter_android.retrofit

import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.model.UserRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthController {

    @POST("/api/auth/sign-up")
    fun signUp(@Body userRegisterRequest: UserRegisterRequest): Call<MessageResponse>
}