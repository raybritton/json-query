package com.raybritton.jsonquery.tools

import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.ext.isValue
import com.raybritton.jsonquery.ext.max
import com.raybritton.jsonquery.ext.min
import com.raybritton.jsonquery.ext.sum
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.utils.ELEMENT

internal fun Any?.filter(query: Query): Any {
    if (this == null) {
        return "NULL"
    }
    return when (this) {
        is LinkedTreeMap<*, *> -> this.filter(query)
        is ArrayList<*> -> this.filter(query)
        else -> this
    }
}

internal fun LinkedTreeMap<*, *>.filter(query: Query): Any {
    return when (query.targetExtra) {
        Query.TargetExtra.KEYS -> ArrayList(keys)
        Query.TargetExtra.VALUES -> ArrayList(values)
        Query.TargetExtra.SPECIFIC -> {
            val iterator = iterator()
            while (iterator.hasNext()) {
                val pair = iterator.next()
                if (!query.targetKeys.contains(pair.key)) {
                    iterator.remove()
                }
            }
            if (size == 1) {
                return this
            } else {
                this
            }
        }
        else -> this
    }
}

internal fun ArrayList<*>.getValues(query: Query): ArrayList<*> {
    if (query.targetKeys[0] == ELEMENT) {
        return this
    } else {
        val list = map { it.navigate(query.targetKeys[0]) }
        return ArrayList(list)
    }
}

internal fun ArrayList<*>.filter(query: Query): Any {
    var list = where(query)
    list = list.skip(query).limit(query)
    if (query.distinct) {
        list = ArrayList(list.distinct())
    }
    return when (query.targetExtra) {
        Query.TargetExtra.MIN -> list.getValues(query).min()
        Query.TargetExtra.MAX -> list.getValues(query).max()
        Query.TargetExtra.COUNT -> list.getValues(query).size
        Query.TargetExtra.SUM -> list.getValues(query).sum()
        else -> list
    }
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
    if (query.offset == null) {
        return this
    } else {
        val from = Math.min(query.offset, size)
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
            if (this == where.compare) {
                return true
            }
        }
        Query.Where.Operator.NOT_EQUAL -> {
            if (this != where.compare) {
                return true
            }
        }
        Query.Where.Operator.LESS_THAN -> {
            if (this is String) {
                if (this < where.compare as String) {
                    return true
                }
            } else if (this != null && (this as Double) < (where.compare as Double)) {
                return true
            }
        }
        Query.Where.Operator.GREATER_THAN -> {
            if (this is String) {
                if (this < where.compare as String) {
                    return true
                }
            } else if (this != null && (this as Double) > (where.compare as Double)) {
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