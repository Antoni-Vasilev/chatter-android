package eu.nexanet.chatter_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import eu.nexanet.chatter_android.R
import eu.nexanet.chatter_android.activity.ForgotPasswordActivity
import eu.nexanet.chatter_android.model.MessageResponse
import eu.nexanet.chatter_android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordCheckFragment(
    val nextFragment: ForgotPasswordGenerateFragment.FragmentCallback
) : Fragment() {

    private lateinit var field1: TextInputLayout
    private lateinit var field2: TextInputLayout
    private lateinit var field3: TextInputLayout
    private lateinit var field4: TextInputLayout
    private lateinit var field5: TextInputLayout
    private lateinit var field6: TextInputLayout
    private lateinit var btnNext: Button

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_forgot_password_check, container, false)

        init()

        return v
    }

    private fun init() {
        field1 = v.findViewById(R.id.field1)
        field2 = v.findViewById(R.id.field2)
        field3 = v.findViewById(R.id.field3)
        field4 = v.findViewById(R.id.field4)
        field5 = v.findViewById(R.id.field5)
        field6 = v.findViewById(R.id.field6)
        btnNext = v.findViewById(R.id.btnNext)

        fieldSetAction(field1, null, field2)
        fieldSetAction(field2, field1, field3)
        fieldSetAction(field3, field2, field4)
        fieldSetAction(field4, field3, field5)
        fieldSetAction(field5, field4, field6)
        fieldSetAction(field6, field5, null)
        btnNext.setOnClickListener { btnNextAction() }
    }

    private fun fieldSetAction(
        field: TextInputLayout,
        prevField: TextInputLayout?,
        nextField: TextInputLayout?
    ) {
        field.editText?.doOnTextChanged { _, _, _, count ->
            if (field.editText?.text.toString() != field.editText?.text.toString()
                    .uppercase()
            ) field.editText?.setText(field.editText?.text.toString().uppercase())

            if (count == 0 && prevField != null) prevField.requestFocus()
            else if (count == 1) {
                if (nextField != null) nextField.requestFocus()
                else field.clearFocus()
            }
        }
    }

    private fun btnNextAction() {
        RetrofitService.authController.forgotPasswordCheck(readCode(), ForgotPasswordActivity.email)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(p0: Call<MessageResponse>, p1: Response<MessageResponse>) {
                    if (p1.isSuccessful) {
                        ForgotPasswordActivity.code = readCode()
                        nextFragment.nextFragment()
                    }
                }

                override fun onFailure(p0: Call<MessageResponse>, p1: Throwable) {
                    Toast.makeText(v.context, p1.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun readCode(): String {
        return field1.editText?.text.toString() +
                field2.editText?.text.toString() +
                field3.editText?.text.toString() +
                field4.editText?.text.toString() +
                field5.editText?.text.toString() +
                field6.editText?.text.toString()
    }
}