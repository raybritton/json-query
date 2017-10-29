package com.raybritton.jsonquery.ext

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.navigate
import com.raybritton.jsonquery.utils.ELEMENT

internal fun Any?.sort(query: Query): Any? {
    if (query.order == null) return this
    if (this == null) return this
    return when (this) {
        is ArrayList<*> -> this.sort(query)
        is LinkedTreeMap<*, *> -> this.sort(query)
        else -> this
    }
}

internal fun ArrayList<*>.sort(query: Query): ArrayList<*> {
    if (query.order != null) {
        if (query.order == ELEMENT) {
            sortWith(Comparator<Any> { lhs, rhs ->
                lhs.compareTo(query, rhs)
            })
        } else {
            sortWith(Comparator<Any> { lhs, rhs ->
                lhs.navigate(query.order).compareTo(query, rhs.navigate(query.order))
            })
        }
    }
    return this
}

internal fun LinkedTreeMap<*, *>.sort(query: Query): LinkedTreeMap<*, *> {
    for (key in keys) {
        get(key).sort(query)
    }
    return this
}