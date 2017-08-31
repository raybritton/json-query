package com.raybritton.jsonquery.ext

internal fun ArrayList<*>.min(): Double {
    return getNumbers().min() ?: Double.NaN
}

internal fun ArrayList<*>.max(): Double {
    return getNumbers().max() ?: Double.NaN
}

internal fun ArrayList<*>.sum(): Double {
    return getNumbers().sum()
}

internal fun ArrayList<*>.getNumbers(): List<Double> {
    val numbers = mutableListOf<Double>()
    forEach {
        if (it is Number) {
            numbers.add(it.toDouble())
        }
    }
    return numbers
}