package eu.nexanet.chatter_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import java.util.Date

class LoginActivity : AppCompatActivity() {

    lateinit var fieldEmail: TextInputLayout
    lateinit var fieldPassword: TextInputLayout
    lateinit var btnForgotPassword: TextView
    lateinit var btnSignIn: Button
    lateinit var btnSignUp: TextView
    lateinit var appInfo: TextView

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
        btnSignUp.setOnClickListener { btnSignUpAction(it) }
    }

    private fun btnSignUpAction(v: View) {
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