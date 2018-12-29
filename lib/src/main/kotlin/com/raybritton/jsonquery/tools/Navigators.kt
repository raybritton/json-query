package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.ext.toSegments
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import java.util.*

private class Box<T>(val value: T?)

private val INDEX_ACCESSOR = "^\\[(\\d+)\\]$".toPattern()

internal fun Any.navigateToTargetOrProjection(path: String): Any? {
    return this.navigate(path, false)
}

internal fun Any.navigateToTarget(path: String): Any {
    val result = this.navigate(path, true)
    when (result) {
        is JsonArray, is JsonObject -> return result
        null -> throw RuntimeException("Target path resulted in a null value", RuntimeException.ExtraInfo.NAVIGATED_NULL)
        else -> throw RuntimeException("Target path resulted in a value", RuntimeException.ExtraInfo.NAVIGATED_VALUE)
    }
}

internal fun Any?.navigateToProjection(path: String): Any? {
    if (this == null) return this
    val result = this.navigate(path, false)
    if (result is JsonArray || result is JsonObject) {
        throw RuntimeException("Path ($path) resulted in an object or array", RuntimeException.ExtraInfo.NAVIGATED_NON_VALUE)
    }
    return result
}

private fun Any.navigate(path: String, isTarget: Boolean, previouslyNavigatedPath: String? = null, inArray: Boolean = false): Any? {
    if (path == ".") {
        return this
    }

    val segments = ArrayDeque(path.toSegments())
    val navigated = mutableListOf<String>()
    if (previouslyNavigatedPath != null) {
        navigated.addAll(previouslyNavigatedPath.toSegments())
    }
    var currentJson: Any? = this

    while (segments.isNotEmpty()) {
        val segment = segments.pop()
        val navigatedPath = (navigated + segment).joinToString(".", prefix = if (isTarget) "." else "")
        var result = when (currentJson) {
            is JsonObject -> currentJson.navigate(segment, navigatedPath, inArray)
            is JsonArray -> currentJson.navigate(segment, segments.joinToString("."), navigatedPath)
            else -> this
        }
        navigated.add(segment)
        if (result is List<*> && result.size == 1) {
            result = result[0]
        }
        if (result is Box<*>) {
            result = result.value
        }
        currentJson = result
    }

    return currentJson
}

private fun JsonObject.navigate(segment: String, navigatedPath: String, inArray: Boolean): Box<*>? {
    return if (containsKey(segment)) {
        Box(get(segment))
    } else {
        if (INDEX_ACCESSOR.matcher(segment).matches()) {
            throw RuntimeException("Attempted index access on object with '$navigatedPath'", RuntimeException.ExtraInfo.INDEX_ON_OBJECT)
        } else {
            if (inArray) {
                return null
            } else {
                throw RuntimeException("Found nothing for '$navigatedPath'", RuntimeException.ExtraInfo.NAVIGATED_NOTHING)
            }
        }
    }
}

private fun JsonArray.navigate(segment: String, remainingPath: String, navigatedPath: String): Any {
    val matcher = INDEX_ACCESSOR.matcher(segment)
    return if (matcher.matches()) {
        val index = matcher.group(1).toInt()
        if (index >= size) {
            throw RuntimeException("Index too high for '$navigatedPath'", RuntimeException.ExtraInfo.INDEX_TOO_HIGH)
        } else {
            Box(get(index))
        }
    } else {
        if (any { it is JsonObject }) {
            mapNotNull { (it as? JsonObject)?.navigate(segment, navigatedPath, true) }
        } else {
            throw RuntimeException("No objects found for '$navigatedPath'", RuntimeException.ExtraInfo.FIELD_ON_ARRAY)
        }
    }
}
