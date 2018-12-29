package com.raybritton.jsonquery.parsing.tokens

import com.raybritton.jsonquery.SyntaxException
import java.util.*

/**
 * Used by TokenParser to build tokens
 *
 * @see TokenParser
 */

internal interface TokenParser<T> {
    fun canParse(char: Char): Boolean
    fun parse(charReader: CharParser): T
}

internal object PunctuationParser : TokenParser<Token.PUNCTUATION> {
    override fun canParse(char: Char) = "(),".contains(char)
    override fun parse(charReader: CharParser): Token.PUNCTUATION {
        val char = charReader.peek()
        when (char) {
            ',', '(', ')' -> return Token.PUNCTUATION(charReader.next()!!, charReader.currentPos - 1)
        }
        throw SyntaxException("Invalid symbol '$char' at ${charReader.currentPos}")
    }
}

internal object OperatorParser : TokenParser<Token.OPERATOR> {
    override fun canParse(char: Char) = "=<>!#".contains(char)
    override fun parse(charReader: CharParser): Token.OPERATOR {
        val char = charReader.peek()
        when (char) {
            '#' -> {
                charReader.skip()
                return Token.OPERATOR(Operator.Contains, charReader.currentPos - 1)
            }
            '>' -> {
                if (charReader.extendedPeek(2) == ">=") {
                    charReader.skip()
                    charReader.skip()
                    return Token.OPERATOR(Operator.GreaterThanOrEqual, charReader.currentPos - 2)
                } else {
                    charReader.skip()
                    return Token.OPERATOR(Operator.GreaterThan, charReader.currentPos - 1)
                }
            }
            '<' -> {
                if (charReader.extendedPeek(2) == "<=") {
                    charReader.skip()
                    charReader.skip()
                    return Token.OPERATOR(Operator.LessThanOrEqual, charReader.currentPos - 2)
                } else {
                    charReader.skip()
                    return Token.OPERATOR(Operator.LessThan, charReader.currentPos - 1)
                }
            }
            '!' -> {
                if (charReader.extendedPeek(2) == "!#") {
                    charReader.skip()
                    charReader.skip()
                    return Token.OPERATOR(Operator.NotContains, charReader.currentPos - 2)
                }
                if (charReader.extendedPeek(2) == "!=") {
                    charReader.skip()
                    charReader.skip()
                    return Token.OPERATOR(Operator.NotEqual, charReader.currentPos - 2)
                }
            }
            '=' -> {
                if (charReader.extendedPeek(2) == "==") {
                    charReader.skip()
                    charReader.skip()
                    return Token.OPERATOR(Operator.Equal, charReader.currentPos - 2)
                } else {
                    charReader.skip()
                    return Token.OPERATOR(Operator.Equal, charReader.currentPos - 1)
                }
            }
        }
        throw SyntaxException("Invalid symbol '$char' at ${charReader.currentPos}")
    }
}

internal object WhitespaceParser : TokenParser<Token.WHITESPACE> {
    override fun canParse(char: Char) = char.isWhitespace()
    override fun parse(charReader: CharParser): Token.WHITESPACE {
        while (charReader.peek() != null && canParse(charReader.peek()!!)) {
            charReader.next()
        }
        return Token.WHITESPACE(charReader.currentPos)
    }
}

internal object NumberParser : TokenParser<Token.NUMBER> {
    override fun canParse(char: Char) = char.let { it.isDigit() || it == '-' }
    override fun parse(charReader: CharParser): Token.NUMBER {
        var next = charReader.peek()
        val output = StringBuilder()

        var hasDot = false
        var isNegative = false

        val make: () -> Token.NUMBER = {
            Token.NUMBER(output.toString().toDouble() * if (isNegative) -1 else 1, charReader.currentPos - output.length)
        }

        while (next != null) {
            if (next == '-') {
                isNegative = true
            } else if (next == '.') {
                if (hasDot) {
                    return make()
                } else {
                    hasDot = true
                    output.append(charReader.next()!!)
                }
            } else if (next.isDigit()) {
                output.append(charReader.next()!!)
            } else {
                return make()
            }
            next = charReader.peek()
        }

        throw UnsupportedOperationException("Not reachable? $output")
//        return Token.NUMBER(output.toString().toDouble(), charReader.currentPos - output.length)
    }
}

internal object KeywordParser : TokenParser<Token.KEYWORD> {
    override fun canParse(char: Char) = char.isLetter()
    override fun parse(charReader: CharParser): Token.KEYWORD {
        val startIdx = charReader.currentPos
        var next = charReader.peek()
        val output = StringBuilder()
        while (next != null) {
            if (charReader.peek() != null && canParse(charReader.peek()!!)) {
                output.append(charReader.next()!!)
            } else {
                next = null
            }
        }

        if (Keyword.isValid(output.toString().toUpperCase(Locale.US))) {
            return Token.KEYWORD(Keyword.valueOf(output.toString().toUpperCase(Locale.US)), charReader.currentPos - output.length)
        } else {
            throw SyntaxException("Invalid KEYWORD '$output' at $startIdx")
        }
    }
}

internal object StringParser : TokenParser<Token.STRING> {
    override fun canParse(char: Char) = char == '"' || char == '\''
    override fun parse(charReader: CharParser): Token.STRING {
        var escaped = false
        var str = ""
        val endingMark = charReader.next()!!
        while (!charReader.isEof()) {
            val ch = charReader.next()
            if (escaped) {
                str += ch
                escaped = false
            } else if (ch == '\\') {
                escaped = true
            } else if (ch == endingMark) {
                break
            } else {
                str += ch
            }
        }
        return Token.STRING(str, charReader.currentPos - str.length)
    }
}