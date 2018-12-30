package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.Value
import java.util.*

internal fun Any?.compareTo(query: Query, rhs: Any?): Int {
    if (this == null && rhs != null) {
        return if (query.flags.isOrderByDesc) -1 else 1
    }
    if (this != null && rhs == null) {
        return if (query.flags.isOrderByDesc) 1 else -1
    }
    if (this is Number && rhs is Number) {
        var result = this.toDouble().compareTo(rhs.toDouble())
        if (query.flags.isOrderByDesc) {
            result *= -1
        }
        return result
    } else if (this is String && rhs is String) {
        var result = this.compareTo(rhs, !query.flags.isCaseSensitive)
        if (query.flags.isOrderByDesc) {
            result *= -1
        }
        return result
    }
    return 0
}

internal fun Any?.isSameValueAs(rhs: Value<*>, caseSensitive: Boolean): Boolean {
    if (rhs is Value.ValueNull && this == null) {
        return true
    } else if (this is Number && rhs is Value.ValueNumber) {
        return this.toDouble() == rhs.value
    } else if (this is String && rhs is Value.ValueString) {
        return this.equals(rhs.value, !caseSensitive)
    } else if (this is String && rhs is Value.ValueNumber) {
        val converted = this.toDoubleOrNull()
        if (converted != null) {
            return rhs.value == converted
        }
    } else if (this is Boolean && rhs is Value.ValueBoolean) {
        return this == rhs.value
    } else if (this is Number && rhs is Value.ValueString) {
        val converted = rhs.value.toDoubleOrNull()
        if (converted != null) {
            return this.toDouble() == converted
        }
    } else if (this is Boolean && rhs is Value.ValueString) {
        return this.toString().toLowerCase(Locale.US) == rhs.value.toLowerCase(Locale.US)
    } else if (this is String && rhs is Value.ValueBoolean) {
        return this.toLowerCase(Locale.US) == rhs.value.toString().toLowerCase(Locale.US)
    }
    return false
}