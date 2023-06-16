package fr.klso.gulpi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Computer(
    override val id: Int,
    override val name: String,
    val serial: String,
    @SerialName("otherserial")
    val assetTag: String
) : Item()
