package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.models.Value
import java.util.*

internal fun Any?.compareWith(isOrderByDesc: Boolean, isCaseSensitive: Boolean, rhs: Any?): Int {
    if ((this == null || this is Unit) && (rhs == null || rhs is Unit)) {
        return 0
    }
    if ((this == null || this is Unit) && rhs != null) {
        return if (isOrderByDesc) -1 else 1
    }
    if (this != null && (rhs == null || rhs is Unit)) {
        return if (isOrderByDesc) 1 else -1
    }
    if (this is Boolean && rhs is Boolean) {
        @Suppress("USELESS_CAST") //Without both 'as Boolean' Kotlin tries to cast them to numbers see https://youtrack.jetbrains.net/issue/KT-29088
        val result = (this as Boolean).compareTo(rhs as Boolean)
        return if (isOrderByDesc) result * -1 else result
    }
    if (this is Number && rhs is Number) {
        val result = this.toDouble().compareTo(rhs.toDouble())
        return if (isOrderByDesc) result * -1 else result
    } else if (this is String && rhs is String) {
        val result = this.compareTo(rhs, !isCaseSensitive)
        return if (isOrderByDesc) result * -1 else result
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