package com.raybritton.jsonquery.ext

fun <T> List<T>.copyWithoutLast(): List<T> {
    if (this.size < 2) {
        return listOf()
    } else {
        return subList(0, this.size - 1)
    }
}

fun <T> List<T>.copyWithoutFirst(): List<T> {
    if (this.size < 2) {
        return listOf()
    } else {
        return subList(1, this.size)
    }
}

fun List<String>.toPath(): String {
    if (this.isEmpty()) {
        return "."
    } else {
        return this.joinToString(".")
    }
}