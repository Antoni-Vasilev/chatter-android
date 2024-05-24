package eu.nexanet.chatter_android.model

import java.util.Date

data class UserSessionData(
    var id: String,
    var username: String,
    var firstName: String,
    var lastName: String,
    var createDate: Date,
    var lastOnline: Date,
    var lastProfileImageUpdate: Date?,
    var roles: List<Role>,
    var devices: List<Device>,
    var emailValidate: Boolean,
    var email: String
)
