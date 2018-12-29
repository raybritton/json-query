package com.raybritton.jsonquery.ext

private val NON_ESCAPED_DOT = "(?<!\\\\)(\\.)".toRegex()

internal fun String.toSegments(): List<String> {
    val result = this.split(NON_ESCAPED_DOT)
            .map {
                it.replace("\\.", ".")
                        .replace("\\\"", "\"")
                        .replace("\'", "'")
            }
            .toMutableList()

    if (result[0].isEmpty()) {
        result.removeAt(0)
    }

    return result
}