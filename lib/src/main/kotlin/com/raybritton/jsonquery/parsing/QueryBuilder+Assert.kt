package com.raybritton.jsonquery.parsing

import com.raybritton.jsonquery.SyntaxException
import com.raybritton.jsonquery.ext.joinStringOr
import com.raybritton.jsonquery.models.Query

internal fun Query.Builder.allowedMethods(keyword: String, vararg allowedMethods: Query.Method) {
    checkMethodSet()
    if (!allowedMethods.contains(method)) {
        throw SyntaxException("$keyword only works with ${allowedMethods.joinStringOr()}")
    }
}

internal fun Query.Builder.checkMethodSet() {
    if (method == null) {
        throw SyntaxException("Method (SELECT, DESCRIBE or SEARCH) must be first")
    }
}

internal fun Query.Builder.checkMethodNotSet() {
    if (method != null) {
        throw SyntaxException("Method can only be set once, already set to $method")
    }
}

internal fun Query.Builder.checkTargetExtras(keyword: String, vararg allowedExtras: Query.TargetExtra) {
    if (targetExtra != null && !allowedExtras.contains(targetExtra)) {
        if (allowedExtras.size == 1 && allowedExtras[0] == Query.TargetExtra.SPECIFIC) {
            throw SyntaxException("$keyword does not support KEYS, VALUES, MIN, MAX, SUM or COUNT")
        } else {
            throw SyntaxException("$keyword only supports ${allowedExtras.filterNot { it == Query.TargetExtra.SPECIFIC }.joinStringOr()}")
        }
    }
}