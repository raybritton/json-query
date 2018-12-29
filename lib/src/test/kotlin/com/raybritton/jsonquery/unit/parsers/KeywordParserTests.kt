package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.parsing.tokens.CharParser
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.parsing.tokens.KeywordParser
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class KeywordParserTests {
    @Test
    fun `test can parse`() {
        val valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val invalid = "1234567890-=!@£$%^&*()#_+[]{};'\\:\"|,./<>?`~"

        for (char in valid) {
            assertTrue(char.toString(), KeywordParser.canParse(char))
        }

        for (char in invalid) {
            assertFalse(char.toString(), KeywordParser.canParse(char))
        }
    }

    @Test
    fun `test simple upper case keyword detection`() {
        val keywords = Keyword.values()

        for (word in keywords) {
            val reader = CharParser(word.name)
            val result = KeywordParser.parse(reader)
            assertEquals(word.name, word, result.value)
        }
    }

    @Test
    fun `test simple lower case keyword detection`() {
        val keywords = Keyword.values()

        for (word in keywords) {
            val reader = CharParser(word.name.toLowerCase(Locale.US))
            val result = KeywordParser.parse(reader)
            assertEquals(word.name, word, result.value)
        }
    }

    @Test
    fun `test simple mixed case keyword detection`() {
        val mixer: (String) -> String = {
            it.mapIndexed { i, c -> if (i % 2 == 0) c.toUpperCase() else c }.joinToString("")
        }

        val keywords = Keyword.values()

        for (word in keywords) {
            val reader = CharParser(mixer(word.name))
            val result = KeywordParser.parse(reader)
            assertEquals(word.name, word, result.value)
        }
    }
}