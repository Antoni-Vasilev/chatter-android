package eu.nexanet.chatter_android.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.model.UserRegisterRequest
import eu.nexanet.chatter_android.readField
import eu.nexanet.chatter_android.retrofit.RetrofitService
import eu.nexanet.chatter_android.toSHA256
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var btnBack: LinearLayout
    lateinit var fieldFullName: TextInputLayout
    lateinit var fieldEmail: TextInputLayout
    lateinit var fieldPassword: TextInputLayout
    lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        setAction()
    }

    private fun init() {
        btnBack = findViewById(R.id.btnBack)
        fieldFullName = findViewById(R.id.fieldFullName)
        fieldEmail = findViewById(R.id.fieldEmail)
        fieldPassword = findViewById(R.id.fieldPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
    }

    private fun setAction() {
        btnBack.setOnClickListener { btnBackOnClick() }
        btnSignUp.setOnClickListener { btnSignUpOnClick() }
    }

    private fun btnBackOnClick() {
        finish()
    }

    private fun btnSignUpOnClick() {
        val fullName = fieldFullName.readField()
        val email = fieldEmail.readField()
        val password = fieldPassword.readField().toSHA256()

        val data = UserRegisterRequest(fullName, email, password)

        RetrofitService.authController.signUp(data)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                    if (p1.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, p1.body()?.message, Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        if (p1.code() <= 499) {
                            val message =
                                Gson().fromJson(
                                    p1.errorBody()?.string(),
                                    MessageResponse::class.java
                                )
                            Toast.makeText(this@SignUpActivity, message.message, Toast.LENGTH_LONG)
                                .show()
                        } else if (p1.code() >= 500) {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Server not available",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                    Toast.makeText(this@SignUpActivity, p1.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}