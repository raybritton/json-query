package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.JsonArray
import com.raybritton.jsonquery.JsonObject
import com.raybritton.jsonquery.ext.isSameValueAs
import com.raybritton.jsonquery.ext.isValue
import com.raybritton.jsonquery.models.Query

internal fun JsonObject.where(query: com.raybritton.jsonquery.models.Query): JsonObject {
    return this
}

internal fun JsonArray.where(query: com.raybritton.jsonquery.models.Query): JsonArray {
    if (query.where != null) {
        val result = kotlin.collections.mutableListOf<Any>()
        if (query.where.field == com.raybritton.jsonquery.utils.ELEMENT) {
            for (element in this) {
                if (element.isValue() && element.matches(query.where, query.caseSensitive)) {
                    result.add(element)
                }
            }
        } else {
            for (element in this) {
                val target = element.navigate(query.where.field)
                if (target.isValue() && target.matches(query.where, query.caseSensitive)) {
                    result.add(element)
                }
            }
        }
        return ArrayList(result)
    } else {
        return this
    }
}

private fun Any?.matches(where: Query.Where, caseSensitive: Boolean): Boolean {
    val value = if (this is Pair<*,*>) second else this
    when (where.operator) {
        Query.Where.Operator.EQUAL -> {
            if (value.isSameValueAs(where.compare, caseSensitive)) {
                return true
            }
        }
        Query.Where.Operator.NOT_EQUAL -> {
            if (!value.isSameValueAs(where.compare, caseSensitive)) {
                return true
            }
        }
        Query.Where.Operator.LESS_THAN -> {
            if (value is String) {
                if (value < where.compare.toString()) {
                    return true
                }
            } else if ((value.toString().toDoubleOrNull() ?: 0.0) < (where.compare.toString().toDoubleOrNull() ?: 0.0)) {
                return true
            }
        }
        Query.Where.Operator.GREATER_THAN -> {
            if (value is String) {
                if (value < where.compare.toString()) {
                    return true
                }
            } else if ((value.toString().toDoubleOrNull() ?: 0.0) > (where.compare.toString().toDoubleOrNull() ?: 0.0)) {
                return true
            }
        }
        Query.Where.Operator.CONTAINS -> {
            if (value != null && (value.toString()).contains(where.compare.toString())) {
                return true
            }
        }
        Query.Where.Operator.NOT_CONTAINS -> {
            if (value == null || !(value.toString()).contains(where.compare.toString())) {
                return true
            }
        }
    }
    return false
}