package com.raybritton.jsonquery

import com.raybritton.jsonquery.parsing.Token

class SyntaxException: IllegalStateException {
    constructor(message: String) : super(message)
    internal constructor(token: Token) : super("Unexpected ${token.type} \"${token.value}\" at ${token.charIdx}")
    internal constructor(token: Token, expected: String) : super("Unexpected ${token.type} \"${token.value}\" at ${token.charIdx}, expected $expected")
}