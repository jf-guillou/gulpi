package fr.klso.gulpi.utilities

import fr.klso.gulpi.models.search.SearchCriteria

fun List<SearchCriteria>.toQueryMap(): MutableMap<String, String> {
    val qs: MutableMap<String, String> = mutableMapOf()
    for ((i, criteria) in this.withIndex()) {
        val criteriaMap = criteria.toMap()
        for ((key, value) in criteriaMap) {
            qs["criteria[$i][$key]"] = value
        }
    }
    return qs
}
