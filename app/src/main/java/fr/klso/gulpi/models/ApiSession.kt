package fr.klso.gulpi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiSession(
    @SerialName("session_token")
    val sessionToken: String
)