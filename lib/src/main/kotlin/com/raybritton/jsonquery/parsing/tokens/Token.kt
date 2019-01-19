package com.raybritton.jsonquery.parsing.tokens

import org.jetbrains.annotations.Contract

/**
 * Represents a logical unit in the query such as SELECT or "hello"
 */

internal sealed class Token<T>(val value: T, val charIdx: Int) {
    class KEYWORD(value: Keyword, charIdx: Int) : Token<Keyword>(value, charIdx)
    class STRING(value: String, charIdx: Int) : Token<String>(value, charIdx)
    class NUMBER(value: Double, charIdx: Int) : Token<Double>(value, charIdx) {
        val isInteger = (value % 1.0 == 0.0)
        fun asInteger() = value.toInt()
    }

    class WHITESPACE(charIdx: Int) : Token<Unit>(Unit, charIdx)
    class PUNCTUATION(value: Char, charIdx: Int) : Token<Char>(value, charIdx)
    class OPERATOR(value: Operator, charIdx: Int) : Token<Operator>(value, charIdx)

    override fun toString(): String {
        return "${javaClass.simpleName} ($value)"
    }

    override fun equals(other: Any?): Boolean {
        return this.value == (other as? Token<*>)?.value
    }
}

internal fun Token<*>?.isKeyword(vararg keyword: Keyword): Boolean {
    return this is Token.KEYWORD && keyword.contains(this.value)
}

internal fun Token<*>?.isPunctuation(vararg char: Char): Boolean {
    return this is Token.PUNCTUATION && char.contains(this.value)
}
