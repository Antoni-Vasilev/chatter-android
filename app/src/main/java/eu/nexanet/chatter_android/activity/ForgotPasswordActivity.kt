package eu.nexanet.chatter_android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.extensions.LocalDatabase
import eu.nexanet.chatter_android.extensions.LocalDatabasePath
import eu.nexanet.chatter_android.extensions.LocalDatabaseValuePath
import eu.nexanet.chatter_android.fragment.ForgotPasswordChangeFragment
import eu.nexanet.chatter_android.fragment.ForgotPasswordCheckFragment
import eu.nexanet.chatter_android.fragment.ForgotPasswordGenerateFragment

class ForgotPasswordActivity : AppCompatActivity() {

    companion object {
        lateinit var email: String
        lateinit var code: String
    }

    private lateinit var btnBack: LinearLayout

    private val deviceLocalDatabase: LocalDatabase =
        LocalDatabase(this, LocalDatabasePath.DEVICE_DATA)

    private var fragmentIndex = 0
    private val fragments = arrayOf(
        ForgotPasswordGenerateFragment { nextFragment() },
        ForgotPasswordCheckFragment { nextFragment() },
        ForgotPasswordChangeFragment {
            deviceLocalDatabase.clear(LocalDatabaseValuePath.SESSION_TOKEN)
            showSplash()
        }
    )

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        setActions()
        updateFragment()
    }

    private fun updateFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameView, fragments[fragmentIndex]).commit()
    }

    private fun nextFragment() {
        fragmentIndex++
        updateFragment()
    }

    private fun init() {
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setActions() {
        btnBack.setOnClickListener { btnBackOnClick() }
    }

    private fun btnBackOnClick() {
        finish()
    }

    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}