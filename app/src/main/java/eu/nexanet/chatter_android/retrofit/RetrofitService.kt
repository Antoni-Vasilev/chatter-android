package eu.nexanet.chatter_android.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    companion object {
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://test-chatter.bg127.eu")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authController: AuthController = retrofit.create(AuthController::class.java)
    }
}