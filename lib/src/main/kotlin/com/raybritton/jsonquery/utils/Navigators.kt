package com.raybritton.jsonquery.utils

import com.google.gson.internal.LinkedTreeMap

/**
 * Gets the first part of the path
 * Group 1: First segment
 * Group 2: Remaining
 */
private val FIRST_SEGMENT = "\\.?((?:\\\\.|[^.])*)(.*)".toPattern()
/**
 * Used to check if segment has unescaped array access notation
 * If this matches the path is refering to just one element in an array
 * Group 1: Path
 * Group 2: Array index
 */
private val INDEX_ACCESS = "((?:\\\\\"|[^\"])*)(?<!\\\\)\\[(\\d)\\]".toPattern()

internal fun Any?.navigate(path: String): Any? {
    if (this == null) {
        throw IllegalStateException("Tried to navigate on null with $path")
    }
    if (path.isEmpty()) {
        return this
    }
    return when (this) {
        is LinkedTreeMap<*, *> -> this.navigate(path)
        is ArrayList<*> -> this.navigate(path)
        else -> throw IllegalStateException("Failed to navigate with $path inside ${this::class.java.simpleName}")
    }
}

internal fun LinkedTreeMap<*, *>.navigate(path: String): Any? {
        var segment = path.getFirstSegment().unescapeDotNotation()
        val arrayMatcher = INDEX_ACCESS.matcher(segment)
        if (arrayMatcher.matches()) { //moving to particular element in array
            return get(arrayMatcher.group(1)).navigate(segment)
        }
        segment = segment.unescapeArrayNotation()
        if (get(segment) != null) {
            if (path.count { it == '.' } > 1) { //continue navigating
                return get(segment)!!.navigate(path.getRemainingPath())
            } else {
                return get(segment)!! //end of journey
            }
        }
        return null
}

internal fun ArrayList<*>.navigate(path: String): Any? {
    val segment = path.getFirstSegment().unescapeDotNotation().unescapeArrayNotation()
    val matcher = INDEX_ACCESS.matcher(segment)
    if (matcher.matches()) {
        val idx = matcher.group(2).toInt()
        return this[idx].navigate(path.getRemainingPath())
    } else {
        throw IllegalStateException("Array index access required")
    }
}

internal fun String.getFirstSegment(): String {
    val matcher = FIRST_SEGMENT.matcher(this)
    if (matcher.matches()) {
        return matcher.group(1)
    } else {
        throw IllegalArgumentException("Unable to get first segment for $this")
    }
}

internal fun String.getRemainingPath(): String {
    val matcher = FIRST_SEGMENT.matcher(this)
    if (matcher.matches()) {
        return matcher.group(2)
    } else {
        return ""
    }
}