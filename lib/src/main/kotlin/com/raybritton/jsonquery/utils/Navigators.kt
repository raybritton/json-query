package com.raybritton.jsonquery.utils

import com.google.gson.internal.LinkedTreeMap

private val FIRST_SEGMENT = ".((?:\\\\.|[^.])*)(.*)".toPattern() //Gets the first part of the path
private val INDEX_ACCESS = "\"((?:\\\\\"|[^\"])*)(?<!\\\\)\\[(\\d)\\]\"".toPattern() //Used to check if segment has unescaped array access notation

internal fun Any?.navigate(path: String): Any {
    if (this == null) {
        throw IllegalStateException("Tried to navigate on null with $path")
    }
    return when (this) {
        is LinkedTreeMap<*, *> -> this.navigate(path)
        else -> throw IllegalStateException("Failed to navigate with $path inside ${this::class.java.simpleName}")
    }
}

internal fun LinkedTreeMap<*, *>.navigate(path: String): Any {
    val segment = path.getFirstSegment().unescape()
    if (get(segment) != null) {
        if (path.count { it == '.' } > 2) {
            return get(segment)!!.navigate(path.getRemainingPath())
        } else {
            return get(segment)!!
        }
    }
    return this
}

internal fun ArrayList<*>.navigate(path: String): Any {
    return this
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