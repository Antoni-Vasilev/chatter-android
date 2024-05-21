package eu.nexanet.chatter_android.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.extensions.LocalDatabase
import eu.nexanet.chatter_android.extensions.LocalDatabasePath
import eu.nexanet.chatter_android.extensions.LocalDatabaseValuePath
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var btnSignOut: MaterialButton

    private val deviceLocalDatabase: LocalDatabase =
        LocalDatabase(this, LocalDatabasePath.DEVICE_DATA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        init()
        setupToolbar()
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        btnSignOut = findViewById(R.id.btnSignOut)

        btnSignOut.setOnClickListener { btnSignOutAction() }
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