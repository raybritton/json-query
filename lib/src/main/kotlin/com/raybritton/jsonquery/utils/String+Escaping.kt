package com.raybritton.jsonquery.utils

internal fun String.unescapeDotNotation(): String {
    return this.replace("\\.", ".")
}

internal fun String.unescapeArrayNotation(): String {
    return this.replace("\\[", "[")
}