package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.parsing.tokens.CharParser
import com.raybritton.jsonquery.parsing.tokens.PunctuationParser
import org.junit.Assert.*
import org.junit.Test

class PunctuationParserTests {
    @Test
    fun `test can parse`() {
        val valid = "(),"
        val invalid = "=!#<>1234567890-@£$%^&*_+[]{};'\\:\"|./?`~qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

        for (char in valid) {
            assertTrue(char.toString(), PunctuationParser.canParse(char))
        }

        for (char in invalid) {
            assertFalse(char.toString(), PunctuationParser.canParse(char))
        }
    }

    @Test
    fun `test simple operator detection`() {
        val punctuation = listOf("(", ")", ",")

        for (symbol in punctuation) {
            val reader = CharParser(symbol)
            val result = PunctuationParser.parse(reader)
            assertEquals(symbol, symbol, result.value.toString())
        }
    }
}