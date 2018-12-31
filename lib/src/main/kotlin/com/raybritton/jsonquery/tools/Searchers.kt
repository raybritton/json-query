package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.ext.isSameValueAs
import com.raybritton.jsonquery.ext.isValue
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.SearchQuery

internal fun Any?.search(query: Query): Any {
    return when (this) {
        is JsonObject -> this.search(query)
        is JsonArray -> this.search(query)
        else -> throw IllegalStateException("Attempted to search $this")
    }
}

private fun JsonObject.search(query: Query): Any {
    val output = copy()

    for (key in keys) {
        val value = get(key)
        val range = query.search!!.targetRange
        if (value is SearchQuery) throw RuntimeException("Query failed to parse: ${query.originalString}", RuntimeException.ExtraInfo.SEARCH_QUERY)
        if (range == SearchQuery.TargetRange.KEY || range == SearchQuery.TargetRange.ANY) {
            if (!key.isSameValueAs(query.search.value, query.flags.isCaseSensitive)) {
                output.remove(key)
            }
        }
        if (range == SearchQuery.TargetRange.VALUE || range == SearchQuery.TargetRange.ANY) {
            if (!value.isValue() || !value.isSameValueAs(query.search.value, query.flags.isCaseSensitive)) {
                output.remove(key)
            }
        }
        if (!value.isValue()) {
            output[key] = value.search(query)
        }
    }
    return output
}

private fun JsonArray.search(query: Query): Any {
    val output = copy()

    forEachIndexed { _, value ->
        if (value is SearchQuery) throw RuntimeException("Query failed to parse: ${query.originalString}", RuntimeException.ExtraInfo.SEARCH_QUERY)
        when (query.search!!.targetRange) {
            SearchQuery.TargetRange.KEY -> {
            }
            SearchQuery.TargetRange.VALUE, SearchQuery.TargetRange.ANY -> {
                if (!value.isValue() || !value.isSameValueAs(query.search.value, query.flags.isCaseSensitive)) {
                    this.remove(value)
                }
            }
        }
        if (!value.isValue()) {
            output.add(value.search(query))
        }
    }
    return output
}