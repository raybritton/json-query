package com.raybritton.jsonquery.parsing

import com.raybritton.jsonquery.models.Query

fun List<Token>.parse(): Query {
    val builder = Query.Builder()

    var nextIsLimit = false
    var nextIsOffset = false
    var nextIsFrom = false

    forEach {
        if (nextIsLimit) checkIfInteger("LIMIT", it)
        if (nextIsOffset) checkIfInteger("OFFSET", it)

        when (it.type) {
            Token.Type.KEYWORD -> {
                when (it.value) {
                    "SELECT", "DESCRIBE", "SEARCH" -> {
                        builder.method = Query.Method.valueOf(it.value)
                        nextIsFrom = true
                    }
                    "AS_JSON" -> builder.asJson = true
                    "PRETTY" -> builder.pretty = true
                    "WITH_KEYS" -> builder.withKeys = true
                    "DISTINCT" -> builder.distinct = true
                    "LIMIT" -> nextIsLimit = true
                    "OFFSET" -> nextIsOffset = true
                }
            }
            Token.Type.STRING -> TODO()
            Token.Type.NUMBER -> {
                if (nextIsLimit) {
                    builder.limit = it.value.toInt()
                }
                if (nextIsOffset) {
                    builder.offset = it.value.toInt()
                }
            }
            Token.Type.WHITESPACE -> { }
            Token.Type.PUNCTUATION -> TODO()
        }
    }

    return builder.build()
}

private fun checkIfInteger(field: String, token: Token) {
    if (token.type != Token.Type.NUMBER) throw IllegalStateException("$field must be followed a number (integer)")
    if (isInteger(token.value.toFloat())) throw IllegalStateException("$field must be followed an integer")
}

private fun isInteger(float: Float) = float == float.toInt().toFloat()