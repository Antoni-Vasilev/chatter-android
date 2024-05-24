package eu.nexanet.chatter_android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.extensions.LocalDatabase
import eu.nexanet.chatter_android.extensions.LocalDatabasePath
import eu.nexanet.chatter_android.extensions.LocalDatabaseValuePath
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.model.UserSessionData
import eu.nexanet.chatter_android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var btnSignOut: MaterialButton
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView

    private val deviceLocalDatabase: LocalDatabase =
        LocalDatabase(this, LocalDatabasePath.DEVICE_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        init()
        setupToolbar()
        loadUserData()
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        btnSignOut = findViewById(R.id.btnSignOut)
        profileImage = navView.getHeaderView(0).findViewById(R.id.profileImage)
        profileName = navView.getHeaderView(0).findViewById(R.id.profileName)
        profileEmail = navView.getHeaderView(0).findViewById(R.id.profileEmail)

        btnSignOut.setOnClickListener { btnSignOutAction() }
    }

    private fun loadUserData() {
        RetrofitService.authController.account(
            deviceLocalDatabase.read(LocalDatabaseValuePath.SESSION_TOKEN).toString()
        ).enqueue(object : Callback<UserSessionData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(p0: Call<UserSessionData>, p1: Response<UserSessionData>) {
                if (p1.isSuccessful) {
                    Glide.with(this@HomeActivity).load(
                        RetrofitService.retrofit.baseUrl()
                            .toString() + "api/auth/profileImage/" + p1.body()?.id
                    ).into(profileImage)
                    profileName.text = p1.body()?.firstName + " " + p1.body()?.lastName
                    profileEmail.text = p1.body()?.email
                }
            }

            override fun onFailure(p0: Call<UserSessionData>, p1: Throwable) {
                Toast.makeText(this@HomeActivity, p1.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        setTitle("Chats")

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun btnSignOutAction() {
        RetrofitService.authController.logout(
            deviceLocalDatabase.read(LocalDatabaseValuePath.SESSION_TOKEN).toString()
        ).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                if (p1.isSuccessful) showSplash()
                else {
                    val response =
                        Gson().fromJson(p1.errorBody()!!.string(), MessageResponse::class.java)
                    Toast.makeText(this@HomeActivity, response.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                Toast.makeText(this@HomeActivity, p1.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}