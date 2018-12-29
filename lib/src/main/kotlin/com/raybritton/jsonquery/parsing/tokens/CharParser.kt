package com.raybritton.jsonquery.parsing.tokens

import com.raybritton.jsonquery.JQLogger

/**
 * Turns a string into a peekable stream
 */

internal class CharParser(string: String) {
    private val chars = string.toCharArray()
    var currentPos = 0
        private set

    fun peek(): Char? {
        if (currentPos >= chars.size) {
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

    fun skip() {
        JQLogger.info("Skipping (${chars[currentPos]}) to ${currentPos + 1}")
        currentPos++
    }

    fun next(): Char? {
        if (currentPos >= chars.size) {
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
