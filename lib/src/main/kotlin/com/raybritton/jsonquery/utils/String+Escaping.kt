package com.raybritton.jsonquery.utils

internal fun String.unescape(): String {
    return this.replace("\\.", ".")
            .replace("\\[", "[")
}