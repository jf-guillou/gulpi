package fr.klso.gulpi.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchComputer(
    @SerialName("1")
    val name: String,
    @SerialName("2")
    val id: Int,
    @SerialName("5")
    val serial: String,
    @SerialName("6")
    val assetTag: String,
) : SearchItem() {
    companion object {
        val columns = intArrayOf(2, 5, 6)
    }
}