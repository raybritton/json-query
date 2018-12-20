package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.JsonArray

internal fun Iterable<Any?>.minValue(): Double {
    return getNumbers().min() ?: Double.NaN
}

internal fun Iterable<Any?>.maxValue(): Double {
    return getNumbers().max() ?: Double.NaN
}

internal fun Iterable<Any?>.sumAll(): Double {
    return getNumbers().sum()
}

internal fun Iterable<Any?>.getNumbers(): List<Double> {
    val numbers = mutableListOf<Double>()
    forEach {
        if (it is Number) {
            numbers.add(it.toDouble())
        }
    }
    return numbers
}