package com.raybritton.jsonquery.parsing

fun String.parse(): List<Token> {
    val reader = TokenReader(CharReader(this))

    val list = mutableListOf<Token>()

    val peeker = {
//        reader.peek()
        true
    }

    while (peeker() && reader.peek() != null) {
        list.add(reader.next()!!)
    }

    return list.filter { it.type != Token.Type.WHITESPACE }
}

private class TokenReader(private val charReader: CharReader) {

    private val parsers = listOf(
            WhitespaceParser,
            NumberParser,
            StringParser,
            KeywordParser
    )

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
        return null
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
            } else if (next.isDigit()){
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

    override fun canParse(charReader: CharReader) = charReader.peek()?.isLetter() ?: false
    override fun parse(charReader: CharReader): Token? {
        var next = charReader.peek()
        val output = StringBuilder()
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
        KEYWORD, STRING, NUMBER, OPERATOR, WHITESPACE
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