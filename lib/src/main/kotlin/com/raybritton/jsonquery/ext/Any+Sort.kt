package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.models.ElementFieldProjection
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.navigateToProjection

internal fun Any?.sort(query: Query): Any? {
    if (this == null) return this
    if (query.select?.orderBy == null) return this

    return when (this) {
        is JsonArray -> this.sort(query)
        else -> this
    }
}

private fun JsonArray.sort(query: Query): JsonArray {
    val order = query.select!!.orderBy
    if (order != null) {
        if (order is ElementFieldProjection.Element) {
            sortWith(Comparator { lhs, rhs ->
                lhs.compareWith(query.flags.isOrderByDesc, query.flags.isCaseSensitive, rhs)
            })
        } else if (order is ElementFieldProjection.Field) {
            sortWith(Comparator { lhs, rhs ->
                lhs?.navigateToProjection(order.value).compareWith(query.flags.isOrderByDesc, query.flags.isCaseSensitive, rhs?.navigateToProjection(order.value))
            })
        }
    }
    return this
}
