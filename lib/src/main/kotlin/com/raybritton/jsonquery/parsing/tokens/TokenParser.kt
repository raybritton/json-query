package com.raybritton.jsonquery.parsing.tokens

import com.raybritton.jsonquery.JQLogger
import com.raybritton.jsonquery.SyntaxException

/**
 * Converts a string into a list of tokens
 *
 * @see CharParser
 * @see Token
 */

internal fun String.toQueryTokens(): List<Token<*>> {
    val reader = TokenReader(CharParser(this))

    val list = mutableListOf<Token<*>>()

    while (!reader.isEof() && reader.peek() != null) {
        list.add(reader.next()!!)
    }

    return list.filterNot { it is Token.WHITESPACE }
}

private class TokenReader(private val charReader: CharParser) {

    private val parsers = listOf(
            WhitespaceParser,
            NumberParser,
            StringParser,
            KeywordParser,
            PunctuationParser,
            OperatorParser
    )

    fun isEof() = charReader.isEof()

    var current: Token<*>? = null

    fun peek(): Token<*>? {
        if (current == null) {
            current = read()
            JQLogger.info("tr peek - setting current")
        }
        JQLogger.info("tr peek - returning $current")
        return current
    }

    private fun read(): Token<*>? {
        parsers.forEach {
            if (charReader.peek() != null && it.canParse(charReader.peek()!!)) {
                val token = it.parse(charReader)
                JQLogger.info("is ${it::class.java.simpleName}")
                JQLogger.info("read - returning $token")
                return token
            }
        }
        JQLogger.info("read - nothing found")
        throw SyntaxException("Unable to parse '${charReader.peek()}' at ${charReader.currentPos}")
    }

    fun next(): Token<*>? {
        val token = current
        if (token != null) {
            JQLogger.info("tr next - returning current $token")
            current = null
            return token
        }
        return read()
    }
}
