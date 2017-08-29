package com.raybritton.jsonquery.printer

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.navigate

internal fun Any?.list(query: Query): String {
    return when (this) {
        is ArrayList<*> -> this.list(query)
        is LinkedTreeMap<*, *> -> this.print(query)
        else -> {
            if (query.withKeys) { //use the key from the query as the actual key has been lost by this point
                if (query.targetKeys.isNotEmpty()) {
                    "${query.targetKeys[0]}: $this"
                } else {
                    "${query.target.substring(1)}: $this"
                }
            } else {
                this.print(query)
            }
        }
    }
}

private fun Any?.print(query: Query): String {
    if (this == null) {
        return "null"
    }
    when (this) {
        is LinkedTreeMap<*, *> -> return print(query)
        else -> return this.toString()
    }
}

private fun LinkedTreeMap<*, *>.print(query: Query): String {
    val builder = StringBuilder("{")
    for (key in keys) {
        if (query.withKeys) {
            builder.append(key)
            builder.append(": ")
        }
        builder.append(get(key).print(query))
        builder.append(", ")
    }
    if (builder.length > 1) {
        builder.setLength(builder.length - 2)
    }
    builder.append("}")
    return builder.toString()
}

internal fun ArrayList<*>.list(query: Query): String {
    val builder = StringBuilder(if (size > 1) "[" else "")
    for (element in this) {
        if (query.targetExtra == Query.TargetExtra.SPECIFIC) {
            if (query.targetKeys.size == 1) {
                builder.append(element.navigate(query.targetKeys[0]))
            } else if (query.targetKeys.size > 1) {
                builder.append("{")
                for (key in query.targetKeys) {
                    builder.append(element.navigate(key))
                    builder.append(", ")
                }
                builder.setLength(builder.length - 2)
                builder.append("}")
            }
        } else {
            builder.append(element.print(query))
        }
        builder.append(", ")
    }
    if (builder.length > 1) {
        builder.setLength(builder.length - 2)
    }
    if (size > 1) {
        builder.append("]")
    }
    return builder.toString()
}
