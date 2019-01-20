package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.ext.toSegments
import com.raybritton.jsonquery.models.*

private val INDEX_ACCESSOR = "\\[([0-9])+\\]".toPattern()
private val INDEX_ACCESSOR_PREFIX = "\\[([0-9])+\\].(.+)".toPattern()

internal fun JsonArray.offset(offset: Int): JsonArray {
    if (offset > size) {
        throw RuntimeException("Offset is out of bounds of JsonArray")
    }
    return this.subList(offset, size).toJsonArray()
}

internal fun Any.filterToProjection(query: Query): Any {
    val paths: List<String>? = when {
        query.select?.projection != null -> when (val projection = query.select.projection) {
            is SelectProjection.SingleField -> listOf(projection.field)
            is SelectProjection.MultipleFields -> projection.fields.map { it.first }
            else -> null
        }
        query.describe?.projection != null -> listOf(query.describe.projection)
        else -> null
    }

    if (paths == null) return this

    return when (this) {
        is JsonObject -> this.filterToProjection(paths)
        is JsonArray -> this.filterToProjection(paths)
        else -> this
    }
}

private fun JsonObject.filterToProjection(paths: List<String>): JsonObject {
    val fields = mutableListOf<String>()
    val nested = mutableMapOf<String, MutableList<String>>()

    paths.forEach {
        val segments = it.toSegments()
        if (segments.size > 1) {
            val path = segments.subList(1, segments.size).joinToString(".")
            if (nested.containsKey(segments[0])) {
                nested[segments[0]]!!.add(path)
            } else {
                nested[segments[0]] = mutableListOf(path)
            }
        } else {
            fields.add(it)
        }
    }

    val output = JsonObject()

    for (path in nested) {
        output[path.key] = when (val any = this[path.key]) {
            is JsonObject -> {
                any.filterToProjection(path.value)
            }
            is JsonArray -> {
                any.filterToProjection(path.value)
            }
            else -> {
                throw RuntimeException("Non object/array has path: ${path.value[0]}")
            }
        }
    }

    for (field in fields) {
        output[field] = this[field]
    }

    return output
}

/**
 * Arrays are 'invisible' to projections
 *
 * i.e. for [{x: 12}] the projection 'x' will return 12
 */
private fun JsonArray.filterToProjection(paths: List<String>): JsonArray {
    val indexes = mutableListOf<Int>()
    val paths = paths.toMutableList()
    var iterator = paths.listIterator()
    while (iterator.hasNext()) {
        INDEX_ACCESSOR.matcher(iterator.next()).let {
            if (it.matches()) {
                indexes.add(it.group(1).toInt())
                iterator.remove()
            }
        }
    }

    val copy = JsonArray()

    for (idx in indexes.reversed()) {
        if (idx > this.size) {
            throw RuntimeException("Index access $idx outside of array", RuntimeException.ExtraInfo.INDEX_TOO_HIGH)
        }
        copy.add(0, this[idx])
    }

    if (paths.isEmpty()) {
        return copy
    }

    val indexedPaths = mutableMapOf<Int, MutableList<String>>()
    iterator = paths.listIterator()
    while (iterator.hasNext()) {
        INDEX_ACCESSOR_PREFIX.matcher(iterator.next()).let {
            if (it.matches()) {
                val index = it.group(1).toInt()
                if (indexedPaths[index] == null) {
                    indexedPaths[index] = mutableListOf<String>()
                }
                indexedPaths[index]!!.add(it.group(2))
                iterator.remove()
            }
        }
    }

    for (idx in indexedPaths.keys.reversed()) {
        if (idx > this.size) {
            throw RuntimeException("Index access $idx outside of array", RuntimeException.ExtraInfo.INDEX_TOO_HIGH)
        }
        val element = this[idx]
        when (element) {
            is JsonArray -> copy.add(element.filterToProjection(indexedPaths[idx]!!))
            is JsonObject -> copy.add(element.filterToProjection(indexedPaths[idx]!!))
            else -> throw RuntimeException("Index access $idx on value", RuntimeException.ExtraInfo.NAVIGATED_VALUE)
        }
    }

    if (paths.isEmpty()) {
        return copy
    }

    return copy().map {
        when (it) {
            is JsonArray -> it.filterToProjection(paths)
            is JsonObject -> it.filterToProjection(paths)
            else -> it
        }
    }.toJsonArray()
}

