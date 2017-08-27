package com.raybritton.jsonquery.utils

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.models.Query

internal fun Any?.list(query: Query): String {
    if (!(this is ArrayList<*>)) {
        throw IllegalStateException("Tried to list $this")
    }
    return this.list(query)
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
    val builder = StringBuilder("[")
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
    builder.append("]")
    return builder.toString()
}
