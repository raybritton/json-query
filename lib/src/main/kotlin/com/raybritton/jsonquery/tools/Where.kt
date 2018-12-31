package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.parsing.tokens.Operator

internal fun Any.where(query: Query): Any {
    TODO("Implement this")
//    if (query.where == null) return this
//
//    if (query.where.value is Value.ValueQuery) throw RuntimeException("Query failed to parse: ${query.originalString}", RuntimeException.ExtraInfo.WHERE_QUERY)
//
//    val value = when (query.where.projection) {
//        is WhereProjection.Field -> {
//            if (query.where.operator == Operator.Contains || query.where.operator == Operator.NotContains) {
//                try {
//                    this.navigateToTarget(query.where.projection.value)
//                } catch (e: RuntimeException) {
//                    val result = this.navigateToProjection(query.where.projection.value)
//                    if (result is String) {
//                        result
//                    } else {
//                        throw RuntimeException("Where projection was not object, array or string", RuntimeException.ExtraInfo.WHERE_INVALID)
//                    }
//                }
//            } else {
//                this.navigateToProjection(query.where.projection.value)
//            }
//        }
//        WhereProjection.Element -> if (this !is JsonArray) throw RuntimeException("$this is not an array", RuntimeException.ExtraInfo.ELEMENT_WHERE_OBJECT) else Val
//        is WhereProjection.Math -> this.math(query.where.projection.expr, query.where.projection.field)
//    }
//
//    val matches = query.where.operator.op(value, query.where.value, query.flags.isCaseSensitive)
//
//    if (!matches) {
//        when (this) {
//            is JsonArray -> this.clear()
//            is JsonObject -> this.clear()
//        }
//    }

    return this
}

private fun JsonObject.where(query: Query, value: Any?): Any {
    val output = copy()

    if (!query.where!!.operator.op(value, query.where.value, query.flags.isCaseSensitive)) {
        val iterator = output.iterator()
        while(iterator.hasNext()) {
            iterator.remove()
        }
    }

    return output
}

private fun JsonArray.where(query: Query, value: Any?): JsonArray {
    val output = copy()

    if (!query.where!!.operator.op(value, query.where.value, query.flags.isCaseSensitive)) {
        val iterator = output.iterator()
        while(iterator.hasNext()) {
            iterator.remove()
        }
    }

    return output
}
