package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject

internal fun Any?.isValue(): Boolean {
    if (this == null) {
        return true
    }
    return when (this) {
        is String -> true
        is Int -> true
        is Float -> true
        is Double -> true
        is Long -> true
        is Boolean -> true
        is JsonObject -> false
        is JsonArray -> false
        is Pair<*, *> -> true
        else -> throw IllegalStateException("Unknown type: ${this::class.java.name}")
    }
}