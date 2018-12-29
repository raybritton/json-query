package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.SyntaxException
import com.raybritton.jsonquery.models.ElementFieldProjection
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.parsing.tokens.Keyword

internal fun Any.math(expr: Keyword, field: ElementFieldProjection): Double {
    if (!expr.isMath()) {
        throw SyntaxException("$expr is not a math function")
    }

    val json = when (field) {
        is ElementFieldProjection.Field -> this.navigateToTarget(field.value)
        ElementFieldProjection.Element -> this
    }

    if (json !is JsonArray) {
        throw RuntimeException("$expr target must be an array")
    }

    val numbers by lazy { json.filter { it is Number }.map { (it as Number).toDouble() } }

    return when (expr) {
        Keyword.SUM -> numbers.sum()
        Keyword.MIN -> numbers.min() ?: Double.NaN
        Keyword.MAX -> numbers.max() ?: Double.NaN
        Keyword.COUNT -> json.size.toDouble()
        else -> Double.NaN
    }
}