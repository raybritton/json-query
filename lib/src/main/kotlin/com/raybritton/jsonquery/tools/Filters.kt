package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.JsonArray
import com.raybritton.jsonquery.JsonObject
import com.raybritton.jsonquery.ext.*
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.utils.ELEMENT

internal fun Any?.filter(query: Query): Any {
    if (this == null) {
        return "NULL"
    }
    return when (this) {
        is JsonObject -> this.filter(query)
        is JsonArray -> this.filter(query)
        else -> this
    }.let {
        if (query.targetExtra == Query.TargetExtra.KEYS || query.targetExtra == Query.TargetExtra.VALUES) {
            it.toFlatList().let {
                if (query.distinct) {
                    it.distinct()
                } else {
                    it
                }
            }
        } else {
            it
        }
    }
}

internal fun Any?.toFlatList(): List<Any?> {
    return when (this) {
        is JsonObject -> {
            val list = mutableListOf<Any?>()
            for(key in keys) { list.addAll(this[key].toFlatList()) }
            list
        }
        is JsonArray -> {
            val list = mutableListOf<Any?>()
            forEach { list.addAll(it.toFlatList()) }
            list
        }
        else -> listOf(this)
    }
}

internal fun JsonObject.toKeys(): List<Any> {
    val list = mutableListOf<Any>()
    forEach<Any, Any> { (key, _) ->
        list.add(key)
        if (this[key] is JsonObject) {
            list.addAll((this[key] as JsonObject).toKeys())
        }
    }
    return list
}

internal fun JsonObject.filter(query: Query): Any {
    val obj = this//.where(query)
    return when (query.targetExtra) {
        Query.TargetExtra.KEYS -> obj.toKeys()
        Query.TargetExtra.VALUES -> ArrayList(obj.values)
        Query.TargetExtra.SPECIFIC -> {
            val iterator = obj.iterator()
            while (iterator.hasNext()) {
                val pair = iterator.next()
                if (!query.targetKeys.contains(pair.key)) {
                    iterator.remove()
                }
            }
            obj
        }
        else -> obj
    }
}



internal fun JsonArray.getValues(query: Query): List<Any?> {
    if (query.targetKeys[0] == ELEMENT) {
        return this
    } else {
        val list = map { it.navigate(query.targetKeys[0]) }
        return ArrayList(list).toFlatList()
    }
}

internal fun JsonArray.filter(query: Query): Any {
    var list = where(query)
    list = list.skip(query).limit(query)
    if (query.distinct) {
        list = ArrayList(list.distinct())
    }
    return when (query.targetExtra) {
        Query.TargetExtra.MIN -> list.getValues(query).minValue()
        Query.TargetExtra.MAX -> list.getValues(query).maxValue()
        Query.TargetExtra.COUNT -> list.getValues(query).size
        Query.TargetExtra.SUM -> list.getValues(query).sumAll()
        Query.TargetExtra.KEYS, Query.TargetExtra.VALUES -> list.map { it.filter(query) }
        else -> list
    }
}

private fun JsonArray.skip(query: Query): JsonArray {
    if (query.offset == null) {
        return this
    } else {
        val from = Math.min(query.offset, size)
        return ArrayList(this.subList(from, size))
    }
}

private fun JsonArray.limit(query: Query): JsonArray {
    if (query.limit == null) {
        return this
    } else {
        val to = Math.min(query.limit, size)
        return ArrayList(this.subList(0, to))
    }
}

