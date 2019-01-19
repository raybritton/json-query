package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.parsing.tokens.CharParser
import com.raybritton.jsonquery.parsing.tokens.NumberParser
import org.junit.Assert.*
import org.junit.Test

class NumberParserTests {
    @Test
    fun `test can parse`() {
        val valid = "1234567890-"
        val invalid = "=!#<>@£$%^&*_+[]{};'\\:\"|./?`~qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

        for (char in valid) {
            assertTrue(char.toString(), NumberParser.canParse(char))
        }

        for (char in invalid) {
            assertFalse(char.toString(), NumberParser.canParse(char))
        }
    }

    @Test
    fun `test simple number detection`() {
        val numbers = listOf("1", "10", "01", "-99", "45.32", "-100", "56.1342")

        for (number in numbers) {
            val reader = CharParser(number)
            val result = NumberParser.parse(reader)
            assertEquals(number, number.toDoubleOrNull(), result.value)
        }
    }
}