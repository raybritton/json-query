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