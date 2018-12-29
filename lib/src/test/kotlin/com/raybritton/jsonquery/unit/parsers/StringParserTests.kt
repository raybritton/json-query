package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.parsing.tokens.CharParser
import com.raybritton.jsonquery.parsing.tokens.StringParser
import org.junit.Assert.*
import org.junit.Test

class StringParserTests {
    @Test
    fun `test can parse`() {
        val valid = "\"'"
        val invalid = "1234567890-=!#<>@£$%^&*_+[]{};\\:|./?`~qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

        for (char in valid) {
            assertTrue(char.toString(), StringParser.canParse(char))
        }

        for (char in invalid) {
            assertFalse(char.toString(), StringParser.canParse(char))
        }
    }

    @Test
    fun `test simple string detection`() {
        val strings = listOf("'test'", "\"test\"")

        for (string in strings) {
            val reader = CharParser(string)
            val result = StringParser.parse(reader)
            assertEquals(string, string.drop(1).dropLast(1), result.value)
        }
    }
}