package fr.klso.gulpi.models

import kotlinx.serialization.Serializable

@Serializable
sealed class Item() {
    abstract val id: Int
    abstract val name: String
}
