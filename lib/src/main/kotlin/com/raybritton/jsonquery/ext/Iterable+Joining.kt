package com.raybritton.jsonquery.ext

fun Iterable<*>.joinStringOr(): String {
    return this.joinToString(", ").replaceLast(", ", " or ")
}

fun Iterable<*>.joinStringAnd(): String {
    return this.joinToString(", ").replaceLast(", ", " and ")
}

