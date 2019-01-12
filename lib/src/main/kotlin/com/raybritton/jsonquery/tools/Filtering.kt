package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.ext.toSegments
import com.raybritton.jsonquery.models.*

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

internal fun JsonObject.filterToProjection(paths: List<String>): JsonObject {
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
internal fun JsonArray.filterToProjection(paths: List<String>): JsonArray {
    return copy().map {
        when (it) {
            is JsonArray -> it.filterToProjection(paths)
            is JsonObject -> it.filterToProjection(paths)
            else -> it
        }
    }.toJsonArray()
}