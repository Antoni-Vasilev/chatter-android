package eu.nexanet.chatter_android.model

data class UserRegisterRequest(
    var fullName: String,
    var email: String,
    var password: String
)
