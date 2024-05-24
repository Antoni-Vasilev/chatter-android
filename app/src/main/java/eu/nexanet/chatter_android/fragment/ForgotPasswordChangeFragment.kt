package eu.nexanet.chatter_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.activity.ForgotPasswordActivity
import eu.nexanet.chatter_android.extensions.readField
import eu.nexanet.chatter_android.extensions.toSHA256
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordChangeFragment(
    val onSuccess: ForgotPasswordGenerateFragment.FragmentCallback
) : Fragment() {

    private lateinit var fieldPassword: TextInputLayout
    private lateinit var fieldConfirmPassword: TextInputLayout
    private lateinit var btnChangePassword: Button

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_forgot_password_change, container, false)

        init()

        return v
    }

    private fun init() {
        fieldPassword = v.findViewById(R.id.fieldPassword)
        fieldConfirmPassword = v.findViewById(R.id.fieldConfirmPassword)
        btnChangePassword = v.findViewById(R.id.btnChangePassword)

        btnChangePassword.setOnClickListener { btnChangePasswordAction() }
    }

    private fun btnChangePasswordAction() {
        val password = fieldPassword.readField()
        val confirmPassword = fieldConfirmPassword.readField()

        if (password != confirmPassword) {
            Toast.makeText(v.context, "Passwords do not match", Toast.LENGTH_LONG).show()
            return
        }

        RetrofitService.authController.forgotPasswordChange(
            ForgotPasswordActivity.code,
            ForgotPasswordActivity.email,
            password.toSHA256()
        ).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                if (p1.isSuccessful) {
                    onSuccess.nextFragment()
                } else {
                    Toast.makeText(v.context, Gson().fromJson(p1.errorBody()!!.string(), MessageResponse::class.java).message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                Toast.makeText(v.context, p1.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}