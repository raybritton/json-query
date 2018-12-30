package com.raybritton.jsonquery.parsing.tokens

import com.raybritton.jsonquery.ext.compareWith
import com.raybritton.jsonquery.ext.isSameValueAs
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Value

/**
 * Used to compare two fields/values
 */

internal sealed class Operator(val symbol: String) {
    abstract fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean): Boolean

    object Equal : Operator("==") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean) = lhs.isSameValueAs(rhs, caseSensitive)
    }

    object NotEqual : Operator("!=") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean) = !Equal.op(lhs, rhs, caseSensitive)
    }

    object Contains : Operator("#") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean): Boolean {
            return when {
                (lhs is String && rhs is Value.ValueString) -> lhs.contains(rhs.value, !caseSensitive)
                lhs is JsonArray -> lhs.any { it.isSameValueAs(rhs, caseSensitive) }
                lhs is JsonObject -> lhs.keys.any { it.isSameValueAs(rhs, caseSensitive) }
                else -> false //TODO throw?
            }
        }
    }

    object NotContains : Operator("!#") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean) = !Contains.op(lhs, rhs, caseSensitive)
    }

    object LessThan : Operator("<") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean): Boolean {
            return lhs.compareWith(false, caseSensitive, rhs.value) < 0
        }
    }

    object GreaterThan : Operator(">") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean): Boolean {
            return lhs.compareWith(false, caseSensitive, rhs.value) > 0
        }
    }

    object LessThanOrEqual : Operator("<=") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean): Boolean {
            return lhs.compareWith(false, caseSensitive, rhs.value) < 1
        }
    }

    object GreaterThanOrEqual : Operator(">=") {
        override fun op(lhs: Any?, rhs: Value<*>, caseSensitive: Boolean): Boolean {
            return lhs.compareWith(false, caseSensitive, rhs.value) > -1
        }
    }

}

