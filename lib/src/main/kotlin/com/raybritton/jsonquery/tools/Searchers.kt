package com.raybritton.jsonquery.tools

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.JsonArray
import com.raybritton.jsonquery.JsonObject
import com.raybritton.jsonquery.ext.compareTo
import com.raybritton.jsonquery.ext.isSameValueAs
import com.raybritton.jsonquery.ext.isValue
import com.raybritton.jsonquery.models.Query

internal fun Any?.search(query: Query, path: String): List<String> {
    return when (this) {
        is JsonObject -> this.search(query, path)
        is JsonArray -> this.search(query, path)
        else -> throw IllegalStateException("Attempted to search $this")
    }
}

internal fun JsonObject.search(query: Query, path: String): List<String> {
    val output = mutableListOf<String>()
    val prefix = if (path == ".") "" else path
    for (key in keys) {
        val value = get(key)
        when (query.targetExtra) {
            Query.TargetExtra.KEY -> {
                if (key.isSameValueAs(query.targetKeys[0])) {
                    output.add("$prefix.$key: ${get(key)}")
                }
            }
            Query.TargetExtra.VALUE -> {
                if (value.isValue() && value.isSameValueAs(query.targetKeys[0])) {
                    output.add("$prefix.$key: $value")
                }
            }
            else -> throw IllegalStateException("Search with invalid modifier: ${query.targetExtra}")
        }
        if (!value.isValue()) {
            output.addAll(value.search(query, "$prefix.$key"))
        }
    }
    return output
}

internal fun JsonArray.search(query: Query, path: String = ""): List<String> {
    val output = mutableListOf<String>()
    val prefix = if (path == ".") "" else path
    forEachIndexed { i, value ->
        when (query.targetExtra) {
            Query.TargetExtra.KEY -> { }
            Query.TargetExtra.VALUE -> {
                if (value.isValue() && value.isSameValueAs(query.targetKeys[0])) {
                    output.add("$prefix.[$i]: $value")
                }
            }
            else -> throw IllegalStateException("Search with invalid modifier: ${query.targetExtra}")
        }
        if (!value.isValue()) {
            output.addAll(value.search(query, "$prefix.[$i]"))
        }
    }
    return output
}