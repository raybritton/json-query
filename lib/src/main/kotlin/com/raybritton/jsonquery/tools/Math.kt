package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.SyntaxException
import com.raybritton.jsonquery.models.ElementFieldProjection
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.parsing.tokens.Keyword

internal fun Any.math(expr: Keyword, field: ElementFieldProjection, isByElement: Boolean): Any {
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

    if (json.isEmpty()) return 0.0

    if (json[0] is JsonArray && expr != Keyword.COUNT) {
        if (isByElement) {
            return JsonArray(json.mapNotNull { (it as? JsonArray)?.math(expr) })
        } else {
            val values = JsonArray(json.mapNotNull { it as? JsonArray }.flatten())
            return values.math(expr)
        }
    } else {
        return json.math(expr)
    }
}

private fun JsonArray.toNumbers() = this.mapNotNull { (it as? Number)?.toDouble() }

private fun JsonArray.math(expr: Keyword): Double {
    return when (expr) {
        Keyword.SUM -> this.toNumbers().sum()
        Keyword.MIN -> this.toNumbers().min() ?: Double.NaN
        Keyword.MAX -> this.toNumbers().max() ?: Double.NaN
        Keyword.COUNT -> this.size.toDouble()
        else -> Double.NaN
    }
}