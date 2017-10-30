package com.raybritton.jsonquery.printer

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.ext.sort
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.navigate

internal fun Any?.list(query: Query, isRoot: Boolean = false): String {
    return when (this) {
        is ArrayList<*> -> this.list(query, true)
        is LinkedTreeMap<*, *> -> this.print(query, true)
        else -> {
            if (query.withKeys) { //use the key from the query as the actual key has been lost by this point
                if (query.targetKeys.isNotEmpty()) {
                    "${query.targetKeys[0]}: $this"
                } else {
                    "${query.target.substring(1)}: $this"
                }
            } else {
                this.print(query, true)
            }
        }
    }
}

private fun Any?.print(query: Query, isRoot: Boolean = false): String {
    if (this == null) {
        return "null"
    }
    when (this) {
        is LinkedTreeMap<*, *> -> return print(query, isRoot)
        else -> return this.toString()
    }
}

private fun LinkedTreeMap<*, *>.print(query: Query, isRoot: Boolean = false): String {
    val showMarkers = (size != 1) || !isRoot || query.withKeys
    val builder = StringBuilder(if (showMarkers) "{" else "")
    for (key in keys) {
        if (query.withKeys) {
            builder.append(key)
            builder.append(": ")
        }
        builder.append(get(key).print(query, false))
        builder.append(", ")
    }
    if (builder.length > 1) {
        builder.setLength(builder.length - 2)
    }
    if (showMarkers) {
        builder.append("}")
    }
    return builder.toString()
}

internal fun ArrayList<*>.list(query: Query, isRoot: Boolean = false): String {
    this.sort(query)
    val showMarkers = (size != 1) || !isRoot
    val builder = StringBuilder(if (showMarkers) "[" else "")
    for (element in this) {
        if (query.targetExtra == Query.TargetExtra.SPECIFIC) {
            if (query.targetKeys.size == 1) {
                builder.append(element.navigate(query.targetKeys[0]).list(query, false))
            } else if (query.targetKeys.size > 1) {
                builder.append("{")
                for (key in query.targetKeys) {
                    builder.append(element.navigate(key).list(query, false))
                    builder.append(", ")
                }
                builder.setLength(builder.length - 2)
                builder.append("}")
            }
        } else {
            builder.append(element.print(query, false))
        }
        builder.append(", ")
    }
    if (builder.length > 1) {
        builder.setLength(builder.length - 2)
    }
    if (showMarkers) {
        builder.append("]")
    }
    return builder.toString()
}
