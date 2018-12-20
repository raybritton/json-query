package com.raybritton.jsonquery.ext

fun Array<*>.joinStringOr(): String {
    return this.joinToString(", ").replaceLast(", ", " or ")
}

fun Array<*>.joinStringAnd(): String {
    return this.joinToString(", ").replaceLast(", ", " and ")
}