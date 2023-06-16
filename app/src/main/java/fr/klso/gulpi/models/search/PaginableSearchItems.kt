package fr.klso.gulpi.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PaginableSearchItems() {
    abstract val totalcount: Int
    abstract val count: Int
    abstract val sort: List<String>
    abstract val order: List<String>
//    abstract val data: List<SearchItem>

    @SerialName("content-range")
    abstract val contentRange: String
}