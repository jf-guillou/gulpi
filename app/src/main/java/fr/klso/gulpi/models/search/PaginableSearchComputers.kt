package fr.klso.gulpi.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginableSearchComputers(
    override val totalcount: Int,
    override val count: Int,
    override val sort: List<String>,
    override val order: List<String>,
    @SerialName("content-range")
    override val contentRange: String,
    val data: List<SearchComputer>,
) : PaginableSearchItems()