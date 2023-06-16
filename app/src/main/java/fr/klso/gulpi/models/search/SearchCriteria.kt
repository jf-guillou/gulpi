package fr.klso.gulpi.models.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchCriteria(
    var field: Int? = null,
    var searchtype: String? = null,
    var value: String? = null,
    var link: SearchLink = SearchLink.AND,
    var meta: Boolean? = null,
    var itemtype: String? = null,
    var criteria: List<SearchCriteria>? = null,
) {
    fun toMap(): Map<String, String> {
        val m = mutableMapOf<String, String>()
        if (field != null) {
            m["field"] = field.toString()
        }
        if (searchtype != null) {
            m["searchtype"] = searchtype.toString()
        }
        if (value != null) {
            m["value"] = value.toString()
        }
        m["link"] = link.name
        if (meta != null) {
            m["meta"] = meta.toString()
        }
        if (itemtype != null) {
            m["itemtype"] = itemtype.toString()
        }
        if (criteria != null) {
            m["criteria"] = criteria.toString()
        }

        return m
    }
}