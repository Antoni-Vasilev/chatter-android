package eu.nexanet.chatter_android.model

class UserLoginRequest(
    val email: String,
    val password: String,

    val deviceId: String,
    val deviceName: String,
    val notificationToken: String
)