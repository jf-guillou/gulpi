package fr.klso.gulpi.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginableSearchComputers(
    override val totalcount: Int = 0,
    override val count: Int = 0,
    override val sort: List<String> = listOf(),
    override val order: List<String> = listOf(),
    @SerialName("content-range")
    override val contentRange: String = "",
    val data: List<SearchComputer>,
) : PaginableSearchItems()