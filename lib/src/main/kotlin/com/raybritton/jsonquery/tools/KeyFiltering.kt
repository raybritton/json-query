package com.raybritton.jsonquery.tools

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.JsonArray
import com.raybritton.jsonquery.JsonObject
import com.raybritton.jsonquery.models.Query

/**
 * Remove any element whose key is not in query.targetKeys
 */
internal fun Any?.filterToKeys(query: Query): Any? {
    return when (this) {
        is JsonArray -> this.filterToKeys(query)
        is JsonObject -> this.filterToKeys(query)
        else -> this
    }
}

private fun JsonArray.filterToKeys(query: Query): Any? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next().filterToKeys(query)
        when (item) {
            is JsonArray -> if (item.isEmpty()) iterator.remove()
            is JsonObject -> if (item.isEmpty()) iterator.remove()
        }
    }
    return this
}

private fun JsonObject.filterToKeys(query: Query): Any? {
    if (query.targetKeys.isNotEmpty()) {
        val iterator = iterator()
        while (iterator.hasNext()) {
            val key = iterator.next().key
            if (!query.targetKeys.contains(key)) {
                iterator.remove()
            }
        }
    }
    return this
}