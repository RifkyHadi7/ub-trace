package com.example.ubtrace.data.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Report(
    @SerialName("id")
    val id: String = "",

    @SerialName("userId")
    val userUid: String = "",

    @SerialName("lat")
    val latitude: Double = 0.0,

    @SerialName("long")
    val longitude: Double = 0.0,

    @SerialName("image")
    val image: String = "",

    @SerialName("item")
    val item: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("status")
    val status: String = "",

    @SerialName("noTelp")
    val noTelp: String = ""
)
