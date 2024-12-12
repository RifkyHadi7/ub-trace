package com.example.ubtrace.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("email")
    val email : String = "",

    @SerialName("userId")
    val userUid : String = "",

    @SerialName("username")
    val username : String = "",

    @SerialName("noTelp")
    val noTelp : String = ""
)
