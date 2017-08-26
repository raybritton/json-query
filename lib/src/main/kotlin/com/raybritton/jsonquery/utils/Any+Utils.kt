package com.raybritton.jsonquery.utils

import com.google.gson.internal.LinkedTreeMap

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
        is LinkedTreeMap<*, *> -> false
        is ArrayList<*> -> false
        else -> throw IllegalStateException("Unknown type: ${this::class.java.name}")
    }
}