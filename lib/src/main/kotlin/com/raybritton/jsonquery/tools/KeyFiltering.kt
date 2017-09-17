package com.raybritton.jsonquery.tools

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.models.Query

fun Any?.filterToKeys(query: Query): Any? {
    return when (this) {
        is ArrayList<*> -> this.filterToKeys(query)
        is LinkedTreeMap<*, *> -> this.filterToKeys(query)
        else -> this
    }
}

fun ArrayList<*>.filterToKeys(query: Query): Any? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val item = iterator.next().filterToKeys(query)
        when (item) {
            is ArrayList<*> -> if (item.isEmpty()) iterator.remove()
            is LinkedTreeMap<*, *> -> if (item.isEmpty()) iterator.remove()
        }
    }
    return this
}

fun LinkedTreeMap<*, *>.filterToKeys(query: Query): Any? {
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