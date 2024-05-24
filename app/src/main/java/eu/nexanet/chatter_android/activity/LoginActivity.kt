package eu.nexanet.chatter_android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.extensions.LocalDatabase
import eu.nexanet.chatter_android.extensions.LocalDatabasePath
import eu.nexanet.chatter_android.extensions.LocalDatabaseValuePath
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.model.UserLoginRequest
import eu.nexanet.chatter_android.extensions.readField
import eu.nexanet.chatter_android.retrofit.RetrofitService
import eu.nexanet.chatter_android.extensions.toSHA256
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var fieldEmail: TextInputLayout
    private lateinit var fieldPassword: TextInputLayout
    private lateinit var btnForgotPassword: TextView
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: TextView
    private lateinit var appInfo: TextView

    private val deviceLocalDatabase: LocalDatabase =
        LocalDatabase(this, LocalDatabasePath.DEVICE_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        setupAppInfo()
        setupActions()
    }

    private fun init() {
        fieldEmail = findViewById(R.id.fieldEmail)
        fieldPassword = findViewById(R.id.fieldPassword)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnSignUp = findViewById(R.id.btnSignUp)
        appInfo = findViewById(R.id.appInfo)
    }

    private fun setupActions() {
        btnSignUp.setOnClickListener { btnSignUpAction() }
        btnForgotPassword.setOnClickListener { btnForgotPasswordAction() }
        btnSignIn.setOnClickListener { btnSignInAction() }
    }

    @SuppressLint("HardwareIds")
    private fun btnSignInAction() {
        val email = fieldEmail.readField()
        val password = fieldPassword.readField().toSHA256()

        val data = UserLoginRequest(
            email,
            password,
            Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            Settings.Global.getString(this.contentResolver, Settings.Global.DEVICE_NAME),
            deviceLocalDatabase.read(LocalDatabaseValuePath.NOTIFICATION_TOKEN) ?: ""
        )

        RetrofitService.authController.signIn(data)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                    if (!p1.isSuccessful) {
                        val response =
                            Gson().fromJson(p1.errorBody()?.string(), MessageResponse::class.java)
                        Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        deviceLocalDatabase.write(
                            LocalDatabaseValuePath.SESSION_TOKEN,
                            p1.body()?.message.toString()
                        )
                        showHome()
                    }
                }

                override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                    Toast.makeText(this@LoginActivity, p1.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun showHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun btnForgotPasswordAction() {
        showForgotPassword()
    }

    private fun showForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun btnSignUpAction() {
        showSignUp()
    }

    private fun showSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun setupAppInfo() {
        appInfo.text = "NexaNet © ${Date().year + 1900} | v${
            packageManager.getPackageInfo(
                packageName,
                0
            ).versionName
        }"
    }
}