package com.raybritton.jsonquery.printer

import com.raybritton.jsonquery.JsonArray
import com.raybritton.jsonquery.JsonObject
import com.raybritton.jsonquery.ext.sort
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.navigate

internal fun Any?.print(query: Query, isRoot: Boolean = false): String {
    if (this == null) return "";
    return when (this) {
        is JsonArray -> this.print(query, true)
        is JsonObject -> this.print(query, true)
        else -> {
            if (query.withKeys) { //use the key from the query as the actual key has been lost by this point
                if (query.targetKeys.isNotEmpty()) {
                    "${query.targetKeys[0]}: ${this.wrap()}"
                } else {
                    "${query.target.substring(1)}: ${this.wrap()}"
                }
            } else {
                this.wrap()
            }
        }
    }
}

private fun Any?.wrap(): String {
    if (this is String) {
        return '"' + this + '"'
    } else {
        return this.toString()
    }
}

private fun JsonObject.print(query: Query, isRoot: Boolean = false): String {
    val showMarkers = (size != 1) || !isRoot || query.withKeys
    val builder = StringBuilder(if (showMarkers) "{" else "")
    for (key in keys) {
        val output = get(key).print(query, false)
        if (output.isNotBlank()) {
            if (query.withKeys) {
                builder.append(key)
                builder.append(": ")
            }
            builder.append(output)
            builder.append(", ")
        }
    }
    if (builder.length > 1) {
        builder.setLength(builder.length - 2)
    }
    if (showMarkers) {
        builder.append("}")
    }
    return builder.toString()
}

internal fun JsonArray.print(query: Query, isRoot: Boolean = false): String {
    this.sort(query)
    val showMarkers = (size != 1) || !isRoot
    val builder = StringBuilder(if (showMarkers) "[" else "")
    for (element in this) {
        val startingLength = builder.length
        if (query.targetExtra == Query.TargetExtra.SPECIFIC) {
            if (query.targetKeys.size == 1) {
                val output = element.navigate(query.targetKeys[0]).print(query, false)
                if (output.isNotBlank()) {
                    builder.append(output)
                }
            } else if (query.targetKeys.size > 1) {
                builder.append("{")
                for (key in query.targetKeys) {
                    val output = element.navigate(key).print(query, false)
                    if (output.isNotBlank()) {
                        builder.append(output)
                        builder.append(", ")
                    }
                }
                builder.setLength(builder.length - 2)
                builder.append("}")
            }
        } else {
            builder.append(element.print(query, false))
        }
        if (builder.length > startingLength) {
            builder.append(", ")
        }
    }
    if (builder.length > 1) {
        builder.setLength(builder.length - 2)
    }
    if (showMarkers) {
        builder.append("]")
    }
    return builder.toString()
}
