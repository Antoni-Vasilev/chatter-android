package eu.nexanet.chatter_android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.messaging.FirebaseMessaging
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.extensions.LocalDatabase
import eu.nexanet.chatter_android.extensions.LocalDatabasePath
import eu.nexanet.chatter_android.extensions.LocalDatabaseValuePath
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val deviceLocalDatabase: LocalDatabase =
        LocalDatabase(this, LocalDatabasePath.DEVICE_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNotificationToken()
        checkSession()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show()
            }

            deviceLocalDatabase.write(LocalDatabaseValuePath.NOTIFICATION_TOKEN, task.result)
        }
    }

    private fun checkSession() {
        RetrofitService.authController.refresh(
            deviceLocalDatabase.read(LocalDatabaseValuePath.SESSION_TOKEN).toString()
        )
            .enqueue(object : Callback<MessageResponse> {
                @SuppressLint("HardwareIds")
                override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                    if (!p1.isSuccessful) {
                        RetrofitService.authController.renew(
                            deviceLocalDatabase.read(LocalDatabaseValuePath.SESSION_TOKEN)
                                .toString(),
                            Settings.Secure.getString(
                                this@SplashActivity.contentResolver,
                                Settings.Secure.ANDROID_ID
                            )
                        ).enqueue(object : Callback<MessageResponse> {
                            override fun onResponse(
                                p0: Call<MessageResponse>,
                                p1: Response<MessageResponse>
                            ) {
                                if (p1.isSuccessful) {
                                    deviceLocalDatabase.write(
                                        LocalDatabaseValuePath.SESSION_TOKEN,
                                        p1.body()?.message.toString()
                                    )
                                    showSplash()
                                } else {
                                    deviceLocalDatabase.clear(LocalDatabaseValuePath.SESSION_TOKEN)
                                    showLogin()
                                }
                            }

                            override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                                Toast.makeText(this@SplashActivity, p1.message, Toast.LENGTH_LONG)
                                    .show()
                            }

                        })
                    } else showHome()
                }

                override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                    Toast.makeText(this@SplashActivity, p1.message, Toast.LENGTH_LONG).show()
                    Log.i("MY_DATA", p1.message.toString())
                }
            })
    }

    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}