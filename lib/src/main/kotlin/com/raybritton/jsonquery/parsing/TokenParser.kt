package com.raybritton.jsonquery.parsing

fun String.parse(): MutableList<Token> {
    val reader = TokenReader(CharReader(this))

    val list = mutableListOf<Token>()

    while (reader.peek() != null) {
        list.add(reader.next()!!)
    }

    return list
}

private class TokenReader(private val charReader: CharReader) {

    private val parsers = listOf(
            WhitespaceParser,
            NumberParser,
            StringParser,
            KeywordParser
    )

    var current: Token? = null

    fun isEof() = charReader.isEof()

    fun peek(): Token? {
        if (current == null) {
            current = next()
        }
        return current
    }

    fun next(): Token? {
        val token = current
        if (token != null) {
            current = null
            return token
        }
        parsers.forEach {
            if (it.canParse(charReader)) {
                return it.parse(charReader)
            }
        }
        return null
    }
}

private object WhitespaceParser : TokenParser {
    override fun canParse(charReader: CharReader) = charReader.peek()?.isWhitespace() ?: false
    override fun parse(charReader: CharReader): Token? {
        while (canParse(charReader)) {
            charReader.next()
        }
        return null
    }
}

private object NumberParser : TokenParser {
    override fun canParse(charReader: CharReader) = charReader.peek()?.isDigit() ?: false || charReader.peek() == '.'
    override fun parse(charReader: CharReader): Token {
        var next = charReader.peek()
        val output = StringBuilder()

        while (next != null) {
            next = if (canParse(charReader)) {
                output.append(charReader.next()!!)
                charReader.next()
            } else {
                null
            }
        }

        return Token(Token.Type.NUMBER, output.toString())
    }
}

private object KeywordParser : TokenParser {

    override fun canParse(charReader: CharReader) = charReader.peek()?.isLetter() ?: false
    override fun parse(charReader: CharReader): Token? {
        var next = charReader.peek()
        val output = StringBuilder()
        output.append(next)
        while (next != null) {
            if (canParse(charReader)) {
                output.append(charReader.next()!!)
            } else {
                next = null
            }
        }

        return Token(Token.Type.KEYWORD, output.toString())
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
        KEYWORD, STRING, NUMBER, OPERATOR
    }
}

private interface TokenParser {
    fun canParse(charReader: CharReader): Boolean
    fun parse(charReader: CharReader): Token?
}

private class CharReader(string: String) {
    private val chars = string.toCharArray()
    private var currentPos = 0

    fun peek(): Char? {
        if (currentPos >= chars.size - 1) {
            return null
        }
        return chars[currentPos]
    }

    fun extendedPeek(len: Int): String? {
        if (currentPos >= chars.size - (len + 1)) {
            return null
        }
        return chars.sliceArray(0..len).joinToString("")
    }

    fun next(): Char? {
        if (currentPos >= chars.size - 1) {
            return null
        }
        currentPos++
        return chars[currentPos]
    }

    fun isEof(): Boolean {
        return peek() == null
    }
}
