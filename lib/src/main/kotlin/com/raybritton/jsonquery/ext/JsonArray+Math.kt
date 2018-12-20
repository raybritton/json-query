package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.JsonArray

internal fun JsonArray.min(): Double {
    return getNumbers().min() ?: Double.NaN
}

internal fun JsonArray.max(): Double {
    return getNumbers().max() ?: Double.NaN
}

internal fun JsonArray.sum(): Double {
    return getNumbers().sum()
}

internal fun JsonArray.getNumbers(): List<Double> {
    val numbers = mutableListOf<Double>()
    forEach {
        if (it is Number) {
            numbers.add(it.toDouble())
        }
    }
    return numbers
}