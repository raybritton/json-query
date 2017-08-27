package com.raybritton.jsonquery.utils

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.models.NullCompare
import com.raybritton.jsonquery.models.Query

internal fun Any?.filter(query: Query): Any {
    if (this == null) {
        return "NULL"
    }
    return when (this) {
        is LinkedTreeMap<*, *> -> this.filter(query)
        is ArrayList<*> -> this.filter(query)
        else -> "UNKNOWN"
    }
}

internal fun LinkedTreeMap<*, *>.filter(query: Query): Any {
    return this
}

internal fun ArrayList<*>.filter(query: Query): ArrayList<*> {
    val list = where(query)
    return list.skip(query).limit(query)
}

private fun ArrayList<*>.where(query: Query): ArrayList<*> {
    if (query.where != null) {
        val result = mutableListOf<Any>()
        if (query.where.field == ELEMENT) {
            for (element in this) {
                if (element.isValue() && element.matches(query.where)) {
                    result.add(element)
                }
            }
        } else {
            for (element in this) {
                val target = element.navigate(query.where.field)
                if (target.isValue() && target.matches(query.where)) {
                    result.add(element)
                }
            }
        }
        return ArrayList(result)
    } else {
        return this
    }
}

private fun ArrayList<*>.skip(query: Query): ArrayList<*> {
    if (query.skip == null) {
        return this
    } else {
        val from = Math.min(query.skip, size)
        return ArrayList(this.subList(from, size))
    }
}

private fun ArrayList<*>.limit(query: Query): ArrayList<*> {
    if (query.limit == null) {
        return this
    } else {
        val to = Math.min(query.limit, size)
        return ArrayList(this.subList(0, to))
    }
}

private fun Any?.matches(where: Query.Where): Boolean {
    when (where.operator) {
        Query.Where.Operator.EQUAL -> {
            if (this == where.compare || (this == null && where.compare.javaClass == NullCompare::class.java)) {
                return true
            }
        }
        Query.Where.Operator.NOT_EQUAL -> {
            if (this != where.compare) {
                return true
            }
        }
        Query.Where.Operator.LESS_THAN -> {
            if (this != null && (this as Double) < (where.compare as Double)) {
                return true
            }
        }
        Query.Where.Operator.GREATER_THAN -> {
            if (this != null && (this as Double) > (where.compare as Double)) {
                return true
            }
        }
        Query.Where.Operator.CONTAINS -> {
            if (this != null && (this as String).contains(where.compare as String)) {
                return true
            }
        }
        Query.Where.Operator.NOT_CONTAINS -> {
            if (this == null || !(this as String).contains(where.compare as String)) {
                return true
            }
        }
    }
    return false
}