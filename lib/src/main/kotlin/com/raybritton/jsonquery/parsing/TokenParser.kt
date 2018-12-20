package com.raybritton.jsonquery.parsing

import com.raybritton.jsonquery.JQLogger
import com.raybritton.jsonquery.SyntaxException
import java.util.*

internal fun String.toQueryTokens(): List<Token> {
    val reader = TokenReader(CharReader(this.preParse()))

    val list = mutableListOf<Token>()

    while (!reader.isEof() && reader.peek() != null) {
        list.add(reader.next()!!)
    }

    return list.filter { it.type != Token.Type.WHITESPACE }
}

private fun String.preParse(): String {
    return this.replace("AS JSON", "ASJSON", true)
            .replace("ORDER BY", "ORDERBY", true)
            .replace("CASE SENSITIVE", "CASESENSITIVE", true)
            .replace("WITH VALUES", "WITHVALUES", true)
            .replace("WITH KEYS", "WITHKEYS", true) + " "
}

private class TokenReader(private val charReader: CharReader) {

    private val parsers = listOf(
            WhitespaceParser,
            NumberParser,
            StringParser,
            KeywordParser,
            PunctuationParser
    )

    fun isEof() = charReader.isEof()

    var current: Token? = null

    fun peek(): Token? {
        if (current == null) {
            current = read()
            JQLogger.info("tr peek - setting current")
        }
        JQLogger.info("tr peek - returning $current")
        return current
    }

    private fun read(): Token? {
        parsers.forEach {
            if (it.canParse(charReader)) {
                val token = it.parse(charReader)
                JQLogger.info("is ${it::class.java.simpleName}")
                JQLogger.info("read - returning $token")
                return token
            }
        }
        JQLogger.info("read - nothing found")
        throw SyntaxException("Unable to parse '${charReader.peek()}' at ${charReader.currentPos}")
    }

    fun next(): Token? {
        val token = current
        if (token != null) {
            JQLogger.info("tr next - returning current $token")
            current = null
            return token
        }
        return read()
    }
}

private object PunctuationParser : TokenParser {
    override fun canParse(charReader: CharReader) = "()[],=<>!#".contains(charReader.peek() ?: ' ')
    override fun parse(charReader: CharReader): Token? {
        val char = charReader.peek()
        when (char) {
            ',', '(', ')',  '<', '>', '#' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString(), charReader.currentPos - 1)
            '!' -> {
                if (charReader.extendedPeek(2) == "!#") {
                    return Token(Token.Type.PUNCTUATION, "${charReader.next()}${charReader.next()}", charReader.currentPos - 2)
                }
                if (charReader.extendedPeek(2) == "!=") {
                    return Token(Token.Type.PUNCTUATION, "${charReader.next()}${charReader.next()}", charReader.currentPos - 2)
                }
            }
            '=' -> {
                if (charReader.extendedPeek(2) == "==") {
                    return Token(Token.Type.PUNCTUATION, "${charReader.next()}${charReader.next()}", charReader.currentPos - 2)
                } else {
                    return Token(Token.Type.PUNCTUATION, charReader.next().toString(), charReader.currentPos - 1)
                }
            }
        }
        throw SyntaxException("Invalid symbol '$char' at ${charReader.currentPos}")
    }
}

private object WhitespaceParser : TokenParser {
    override fun canParse(charReader: CharReader) = charReader.peek()?.isWhitespace() ?: false
    override fun parse(charReader: CharReader): Token {
        while (canParse(charReader)) {
            charReader.next()
        }
        return Token(Token.Type.WHITESPACE, "", charReader.currentPos)
    }
}

private object NumberParser : TokenParser {
    override fun canParse(charReader: CharReader) = charReader.peek()?.isDigit() ?: false
    override fun parse(charReader: CharReader): Token {
        var next = charReader.peek()
        val output = StringBuilder()

        var hasDot = false
        while (next != null) {
            if (next == '.') {
                if (hasDot) {
                    return Token(Token.Type.NUMBER, output.toString(), charReader.currentPos - output.length)
                } else {
                    hasDot = true
                    output.append(charReader.next()!!)
                }
            } else if (next.isDigit()) {
                output.append(charReader.next()!!)
            } else {
                return Token(Token.Type.NUMBER, output.toString(), charReader.currentPos - output.length)
            }
            next = charReader.peek()
        }

        return Token(Token.Type.NUMBER, output.toString(), charReader.currentPos - output.length)
    }
}

private object KeywordParser : TokenParser {
    private val KEYWORDS = listOf("SELECT", "DESCRIBE", "DISTINCT", "SUM", "ELEMENT", "FOR", "SEARCH", "KEYS", "VALUES", "SPECIFIC", "MIN", "MAX", "COUNT", "VALUE", "KEY", "LIMIT", "OFFSET", "WITHKEYS", "PRETTY", "ASJSON", "ORDERBY", "WHERE", "DESC", "FROM", "WITHVALUES", "CASESENSITIVE")

    override fun canParse(charReader: CharReader) = charReader.peek()?.isLetter() ?: false
    override fun parse(charReader: CharReader): Token? {
        val startIdx = charReader.currentPos
        var next = charReader.peek()
        val output = StringBuilder()
        while (next != null) {
            if (canParse(charReader)) {
                output.append(charReader.next()!!)
            } else {
                next = null
            }
        }

        if (KEYWORDS.contains(output.toString().toUpperCase(Locale.US))) {
            return Token(Token.Type.KEYWORD, output.toString().toUpperCase(Locale.US), charReader.currentPos - output.length)
        } else {
            throw SyntaxException("Invalid token '$output' at $startIdx")
        }
    }
}

private object StringParser : TokenParser {
    override fun canParse(charReader: CharReader) = charReader.peek() == '"' || charReader.extendedPeek(2) == "\"" || charReader.peek() == '\'' || charReader.extendedPeek(2) == "\'"
    override fun parse(charReader: CharReader): Token {
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
        return Token(Token.Type.STRING, str, charReader.currentPos - str.length)
    }
}

internal data class Token(val type: Type, val value: String, val charIdx: Int) {
    enum class Type {
        KEYWORD, STRING, NUMBER, WHITESPACE, PUNCTUATION
    }

    override fun toString(): String {
        return "$type ($value)"
    }
}

private interface TokenParser {
    fun canParse(charReader: CharReader): Boolean
    fun parse(charReader: CharReader): Token?
}

private class CharReader(string: String) {
    private val chars = string.toCharArray()
    var currentPos = 0
        private set

    fun peek(): Char? {
        if (currentPos >= chars.size - 1) {
            JQLogger.info("cr peek - out of range")
            return null
        }
        JQLogger.info("cr peek - returning ${chars[currentPos].surroundWithQuotes()}")
        return chars[currentPos]
    }

    fun extendedPeek(len: Int): String? {
        if (currentPos >= chars.size - (len + 1)) {
            JQLogger.info("cr extended peek - out of range")
            return null
        }
        JQLogger.info("cr extended peeking at - ${(chars.sliceArray(currentPos until (currentPos + len)).joinToString("")).surroundWithQuotes()}")
        return chars.sliceArray(currentPos until (currentPos + len)).joinToString("")
    }

    fun next(): Char? {
        if (currentPos >= chars.size - 1) {
            JQLogger.info("cr next - out of range")
            return null
        }
        JQLogger.info("cr next - incrementing currentPos, now ${currentPos + 1}")
        val letter = chars[currentPos]
        JQLogger.info("cr next - returning ${letter.surroundWithQuotes()}")
        currentPos++
        return letter
    }

    fun isEof(): Boolean {
        JQLogger.info("cr eof checked: ${peek() == null}")
        return peek() == null
    }
}

private fun String?.surroundWithQuotes(): String? {
    return if (this == null) {
        null
    } else {
        "'" + this + "'"
    }
}

private fun Char?.surroundWithQuotes() = this?.toString()?.surroundWithQuotes()
