package com.raybritton.jsonquery.printer

import com.google.gson.internal.LinkedTreeMap

internal fun Any?.describe(): String {
    if (this == null) {
        return "NULL"
    }
    return when (this) {
        is Int -> "NUMBER"
        is Long -> "NUMBER"
        is Float -> "NUMBER"
        is Double -> "NUMBER"
        is String -> "STRING"
        is Boolean -> "BOOLEAN"
        is LinkedTreeMap<*, *> -> this.describe()
        is ArrayList<*> -> this.describe()
        else -> "UNKNOWN"
    }
}

internal fun LinkedTreeMap<*, *>.describe(): String {
    val builder = StringBuilder("OBJECT(")
    for (value in values) {
        builder.append(value.describe())
        builder.append(", ")
    }
    builder.setLength(builder.length - 2)
    return builder.append(")").toString()
}

internal fun ArrayList<*>.describe(): String {
    val map = mutableMapOf<String, Int>()
    for (i in 0 until size) {
        val desc = this[i].describe()
        if (map.containsKey(desc)) {
            map[desc] = map[desc]!! + 1
        } else {
            map[desc] = 1
        }
    }
    val builder = StringBuilder("ARRAY(")
    for ((key, value) in map) {
        if (value > 1) {
            builder.append(key)
            builder.append("[")
            builder.append(value)
            builder.append("]")
        } else {
            builder.append(key)
        }
        builder.append(", ")
    }
    if (map.isNotEmpty()) {
        builder.setLength(builder.length - 2)
    }
    return builder.append(")").toString()
}