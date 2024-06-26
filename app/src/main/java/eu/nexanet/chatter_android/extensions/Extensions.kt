package eu.nexanet.chatter_android.extensions

import com.google.android.material.textfield.TextInputLayout
import java.security.MessageDigest

fun String.toSHA256(): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return digest.joinToString(
        separator = "",
        transform = { a ->
            String(
                charArrayOf(
                    HEX_CHARS[a.toInt() shr 4 and 0x0f],
                    HEX_CHARS[a.toInt() and 0x0F]
                )
            )
        }
    )
}

fun TextInputLayout.readField(): String {
    return this.editText?.text.toString().trim()
}