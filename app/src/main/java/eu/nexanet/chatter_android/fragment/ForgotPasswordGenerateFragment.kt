@file:Suppress("DEPRECATION")

package eu.nexanet.chatter_android.fragment

import android.app.ProgressDialog
import android.app.ProgressDialog.STYLE_HORIZONTAL
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
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class ForgotPasswordGenerateFragment(
    val nextFragment: FragmentCallback
) : Fragment() {

    private lateinit var fieldEmail: TextInputLayout
    private lateinit var btnNext: Button

    lateinit var v: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_forgot_password_generate, container, false)

        init()

        return v
    }

    private fun init() {
        fieldEmail = v.findViewById(R.id.fieldEmail)
        btnNext = v.findViewById(R.id.btnNext)

        btnNext.setOnClickListener { btnNextAction() }
    }

    private fun btnNextAction() {
        val progressDialog = ProgressDialog(v.context)
        progressDialog.setMessage("The email is being sent. Please wait...")
        progressDialog.max = 100
        progressDialog.setProgressStyle(STYLE_HORIZONTAL)
        progressDialog.isIndeterminate = false
        progressDialog.setCancelable(false)
        progressDialog.show()

        Thread {
            var progress = 0
            while (progress <= 100) {
                Thread.sleep(110)
                progressDialog.progress = progress
                progress++
            }
        }.start()

        RetrofitService.authController.forgotPasswordGenerate(fieldEmail.readField())
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                    progressDialog.dismiss()
                    ForgotPasswordActivity.email = fieldEmail.readField()

                    if (p1.isSuccessful) {
                        nextFragment.nextFragment()
                    } else {
                        Toast.makeText(
                            v.context,
                            Gson().fromJson(
                                p1.errorBody()?.string(),
                                MessageResponse::class.java
                            ).message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                    Toast.makeText(v.context, p1.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun interface FragmentCallback {
        fun nextFragment()
    }
}

