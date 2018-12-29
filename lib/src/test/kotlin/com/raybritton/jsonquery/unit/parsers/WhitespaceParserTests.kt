package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.parsing.tokens.CharParser
import com.raybritton.jsonquery.parsing.tokens.WhitespaceParser
import org.junit.Assert.*
import org.junit.Test

class WhitespaceParserTests {
    @Test
    fun `test can parse`() {
        val valid = " \t"
        val invalid = "1234567890-=!#<>@£$%^&*_+[]{};'\\:\"|./?`~qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

        for (char in valid) {
            assertTrue(char.toString(), WhitespaceParser.canParse(char))
        }

        for (char in invalid) {
            assertFalse(char.toString(), WhitespaceParser.canParse(char))
        }
    }

    @Test
    fun `test simple whitespace detection`() {
        val whitespaces = listOf(" ", "\t")

        for (space in whitespaces) {
            val reader = CharParser(space)
            val result = WhitespaceParser.parse(reader)
            assertNotNull(result)
        }
    }
}