package eu.nexanet.chatter_android.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import eu.nexanet.chatter_android.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btnBack: LinearLayout

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
}