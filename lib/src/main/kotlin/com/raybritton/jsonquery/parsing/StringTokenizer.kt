package com.raybritton.jsonquery.parsing

fun String.tokenize(): List<String> {
    val tokens = mutableListOf<String>()
    var currentToken = ""
    "$this ".forEach { chr ->
        if (chr.isSeparator()) {
            if (currentToken.isNotEmpty()) {
                tokens.add(currentToken.trim(',', ' '))
                currentToken = ""
            }
        } else {
            currentToken += chr
        }
    }
    return tokens
}

fun Char.isSeparator(): Boolean {
    return isWhitespace() || this == '(' || this == ')'
}