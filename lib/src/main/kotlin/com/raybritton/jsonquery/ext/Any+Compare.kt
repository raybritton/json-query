package com.raybritton.jsonquery.ext

import com.raybritton.jsonquery.models.Query

internal fun Any?.compareTo(query: Query, rhs: Any?): Int {
    if (this == null && rhs != null) {
        return if (query.desc) -1 else 1
    }
    if (this != null && rhs == null) {
        return if (query.desc) 1 else -1
    }
    if (this is Number && rhs is Number) {
        var result = this.toDouble().compareTo(rhs.toDouble())
        if (query.desc) {
            result *= -1
        }
        return result
    } else if (this is String && rhs is String) {
        var result = this.compareTo(rhs)
        if (query.desc) {
            result *= -1
        }
        return result
    }
    return 0
}

internal fun Any?.isSameValueAs(rhs: Any?): Boolean {
    if (this == null && rhs != null) {
        return false
    }
    if (this != null && rhs == null) {
        return false
    }
    if (this === rhs) { //this should only be true for nulls
        return true
    }
    if (this is Number && rhs is Number) {
        return this.toDouble() == rhs.toDouble()

    } else if (this is String && rhs is String) {
        return this.equals(rhs, true)
    } else if (this is String && rhs is Number) {
        return this.toDoubleOrNull() ?: 0.0 == rhs.toDouble()
    } else if (this is Number && rhs is String) {
        val converted = rhs.toDoubleOrNull()
        if (converted != null) {
            return this.toDouble() == converted
        }
    }
    return false
}