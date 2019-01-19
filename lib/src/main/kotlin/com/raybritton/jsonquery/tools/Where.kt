package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.models.*

/**
 * Given a json structure
 * Removes parts that do not match query
 */
internal fun Any.where(where: Where, caseSensitive: Boolean, offset: Int?): Any {
    return when (this) {
        is JsonObject -> this.where(where, caseSensitive, offset)
        is JsonArray -> this.where(where, caseSensitive, offset)
        else -> throw RuntimeException("Tried to run where on ${this.javaClass}")
    }
}

private fun JsonObject.where(where: Where, caseSensitive: Boolean, offset: Int?): JsonObject {
    when (where.projection) {
        is WhereProjection.Element -> throw RuntimeException("ELEMENT can not be used with an object", RuntimeException.ExtraInfo.ELEMENT_WHERE_OBJECT)
        is WhereProjection.Field -> {
            if (!where.operator.op(this.navigateToTargetOrProjection(where.projection.value), where.value, caseSensitive)) {
                clear()
            }
        }
        is WhereProjection.Math -> {
            val field = (where.projection.field as ElementFieldProjection.Field).value
            val result = (this.navigateToTargetOrProjection(field) as? JsonArray)?.math(where.projection.expr, ElementFieldProjection.Element, false)
            val matches = if (result != null) {
                where.operator.op(result, where.value, caseSensitive)
            } else {
                false
            }
            if (!matches) {
                clear()
            }
        }
    }
    return this
}

private fun JsonArray.where(where: Where, caseSensitive: Boolean, offset: Int?): JsonArray {
    when (where.projection) {
        is WhereProjection.Element -> {
            return filterUntilSize(offset) { where.operator.op(it, where.value, caseSensitive) }.toJsonArray()
        }
        is WhereProjection.Field -> {
            return filterUntilSize(offset) { where.operator.op(it.navigateToTargetOrProjection(where.projection.value), where.value, caseSensitive) }.toJsonArray()
        }
        is WhereProjection.Math -> {
            return filterUntilSize(offset) { element ->
                val field = (where.projection.field as ElementFieldProjection.Field).value
                val result = (element.navigateToTargetOrProjection(field) as? JsonArray)?.math(where.projection.expr, ElementFieldProjection.Element, false)
                if (result != null) {
                    where.operator.op(result, where.value, caseSensitive)
                } else {
                    false
                }
            }.toJsonArray()
        }
    }
}

private fun JsonArray.filterUntilSize(size: Int?, predicate: (Any?) -> Boolean): JsonArray {
    val output = JsonArray()
    val iterator = iterator()
    while (iterator.hasNext() && (size == null || (output.size < size))) {
        val element = iterator.next()
        if (predicate(element)) {
            output.add(element)
        }
    }
    return output
}