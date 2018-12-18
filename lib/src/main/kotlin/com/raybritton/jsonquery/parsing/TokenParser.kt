package com.raybritton.jsonquery.parsing

import java.util.*

fun String.parse(): List<Token> {
    val reader = TokenReader(CharReader(this.preParse()))

    val list = mutableListOf<Token>()

    while (!reader.isEof() && reader.peek() != null) {
        list.add(reader.next()!!)
    }

    return list.filter { it.type != Token.Type.WHITESPACE }
}

private fun String.preParse(): String {
    return this.replace("AS JSON", "AS_JSON")
            .replace("ORDER BY", "ORDER_BY")
            .replace("WITH KEYS", "WITH_KEYS") + " "
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
            println("tr peek - setting current")
        }
        println("tr peek - returning $current")
        return current
    }

    private fun read(): Token? {
        parsers.forEach {
            if (it.canParse(charReader)) {
                val token = it.parse(charReader)
                println("is ${it::class.java.simpleName}")
                println("read - returning $token")
                return token
            }
        }
        println("read - nothing found")
        throw IllegalStateException("Unable to parse '${charReader.peek()}' at ${charReader.currentPos}")
    }

    fun next(): Token? {
        val token = current
        if (token != null) {
            println("tr next - returning current $token")
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
            '(' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            ')' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            '[' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            ']' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            '<' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            '>' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            '#' -> return Token(Token.Type.PUNCTUATION, charReader.next()!!.toString())
            '!' -> {
                if (charReader.extendedPeek(2) == "!#") {
                    return Token(Token.Type.PUNCTUATION, "${charReader.next()}${charReader.next()}")
                }
                if (charReader.extendedPeek(2) == "!=") {
                    return Token(Token.Type.PUNCTUATION, "${charReader.next()}${charReader.next()}")
                }
            }
            '=' -> {
                if (charReader.extendedPeek(2) == "==") {
                    return Token(Token.Type.PUNCTUATION, "${charReader.next()}${charReader.next()}")
                }
            }
        }
        throw IllegalStateException("Invalid operator '$char' at ${charReader.currentPos}")
    }
}

private object WhitespaceParser : TokenParser {
    override fun canParse(charReader: CharReader) = charReader.peek()?.isWhitespace() ?: false
    override fun parse(charReader: CharReader): Token {
        while (canParse(charReader)) {
            charReader.next()
        }
        return Token(Token.Type.WHITESPACE, "")
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
                    return Token(Token.Type.NUMBER, output.toString())
                } else {
                    hasDot = true
                    output.append(charReader.next()!!)
                }
            } else if (next.isDigit()) {
                output.append(charReader.next()!!)
            } else {
                return Token(Token.Type.NUMBER, output.toString())
            }
            next = charReader.peek()
        }

        return Token(Token.Type.NUMBER, output.toString())
    }
}

private object KeywordParser : TokenParser {
    private val KEYWORDS = listOf("SELECT", "DESCRIBE", "DISTINCT", "SUM", "KEYS", "VALUES", "SPECIFIC", "MIN", "MAX", "COUNT", "VALUE", "KEY", "LIMIT", "OFFSET", "WITH_KEYS", "PRETTY", "AS_JSON", "ORDER_BY", "WHERE", "DESC", "FROM")

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

        if (KEYWORDS.contains(output.toString())) {
            return Token(Token.Type.KEYWORD, output.toString().toUpperCase(Locale.US))
        } else {
            throw IllegalStateException("Invalid token '$output' at $startIdx")
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
        return Token(Token.Type.STRING, str)
    }
}

data class Token(val type: Type, val value: String) {
    enum class Type {
        KEYWORD, STRING, NUMBER, WHITESPACE, PUNCTUATION
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
            println("cr peek - out of range")
            return null
        }
        println("cr peek - return ${quoteIfNotNull(chars[currentPos])}")
        return chars[currentPos]
    }

    fun extendedPeek(len: Int): String? {
        if (currentPos >= chars.size - (len + 1)) {
            println("cr extended peek - out of range")
            return null
        }
        println("cr extended peek - ${quoteIfNotNull(chars.sliceArray(0..len).joinToString(""))}")
        return chars.sliceArray(0..len).joinToString("")
    }

    fun next(): Char? {
        if (currentPos >= chars.size - 1) {
            println("cr next - out of range")
            return null
        }
        println("cr next - incrementing currentPos now ${currentPos + 1}")
        val letter = chars[currentPos]
        println("cr next - return ${quoteIfNotNull(letter)}")
        currentPos++
        return letter
    }

    private fun quoteIfNotNull(str: Char?): String {
        return quoteIfNotNull("" + str)
    }

    fun isEof(): Boolean {
        println("cr eof checked: ${peek() == null}")
        return peek() == null
    }
}

private fun quoteIfNotNull(str: String?): String {
    return if (str == null) {
        "null"
    } else {
        "'$str'"
    }
}