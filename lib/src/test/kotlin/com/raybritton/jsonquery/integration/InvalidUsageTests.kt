package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import com.raybritton.jsonquery.SyntaxException
import jdk.nashorn.internal.runtime.regexp.joni.Syntax
import org.junit.Assert.assertEquals
import org.junit.Test

class InvalidUsageTests {
    @Test
    fun testNoMethodSetWithKeyword() {
        val result = "Method (SELECT, DESCRIBE or SEARCH) must be first"
        val output1 = runQuery("DISTINCT")
        val output2 = runQuery("WITH KEYS")
        val output3 = runQuery("WITH VALUES")
        val output4 = runQuery("AS JSON")
        val output5 = runQuery("LIMIT 1")
        val output6 = runQuery("OFFSET 3")
        val output7 = runQuery("LIMIT")
        val output8 = runQuery("OFFSET")
        val output9 = runQuery("FROM")
        val output10 = runQuery("SUM")
        val output11 = runQuery("MAX")
        val output12 = runQuery("MIN")
        val output13 = runQuery("COUNT")
        val output14 = runQuery("ELEMENT")
        val output15 = runQuery("PRETTY")

        assertEquals("Output 1", result, output1)
        assertEquals("Output 2", result, output2)
        assertEquals("Output 3", result, output3)
        assertEquals("Output 4", result, output4)
        assertEquals("Output 5", result, output5)
        assertEquals("Output 6", result, output6)
        assertEquals("Output 7", result, output7)
        assertEquals("Output 8", result, output8)
        assertEquals("Output 9", result, output9)
        assertEquals("Output 10", result, output10)
        assertEquals("Output 11", result, output11)
        assertEquals("Output 12", result, output12)
        assertEquals("Output 13", result, output13)
        assertEquals("Output 14", result, output14)
        assertEquals("Output 15", result, output15)
    }

    @Test
    fun testNoMethodSetWithValues() {
        val output1 = runQuery("(")
        val output2 = runQuery(")")
        val output3 = runQuery("<")
        val output4 = runQuery(">")
        val output5 = runQuery(",")
        val output6 = runQuery("!=")
        val output7 = runQuery("=")
//        val output8 = runQuery("==")
        val output9 = runQuery("#")
        val output10 = runQuery("!#")
        val output11 = runQuery("'test'")
        val output12 = runQuery("\"test\"")
        val output13 = runQuery("1")
        val output14 = runQuery("45.3")
        val output15 = runQuery(".")
        val output16 = runQuery("true")

        assertEquals("Output 1", "Unexpected PUNCTUATION \"(\" at 0, expected KEYWORD", output1)
        assertEquals("Output 2", "Unexpected PUNCTUATION \")\" at 0, expected KEYWORD", output2)
        assertEquals("Output 3", "Unexpected PUNCTUATION \"<\" at 0, expected KEYWORD", output3)
        assertEquals("Output 4", "Unexpected PUNCTUATION \">\" at 0, expected KEYWORD", output4)
        assertEquals("Output 5", "Unexpected PUNCTUATION \",\" at 0, expected KEYWORD", output5)
        assertEquals("Output 6", "Invalid symbol '!' at 0", output6)
        assertEquals("Output 7", "Unexpected PUNCTUATION \"=\" at 0, expected KEYWORD", output7)
//        assertEquals("Output 8", "Unexpected PUNCTUATION \"==\" at 0, expected KEYWORD", output8)
        assertEquals("Output 9", "Unexpected PUNCTUATION \"#\" at 0, expected KEYWORD", output9)
        assertEquals("Output 10", "Invalid symbol '!' at 0", output10)
        assertEquals("Output 11", "Unexpected STRING \"test\" at 2, expected KEYWORD", output11)
        assertEquals("Output 12", "Unexpected STRING \"test\" at 2, expected KEYWORD", output12)
        assertEquals("Output 13", "Unexpected NUMBER \"1\" at 0, expected KEYWORD", output13)
        assertEquals("Output 14", "Unexpected NUMBER \"45.3\" at 0, expected KEYWORD", output14)
        assertEquals("Output 15", "Unable to parse '.' at 0", output15)
        assertEquals("Output 16", "Invalid token 'true' at 0", output16)
    }

    private fun runQuery(query: String): String {
        try {
            return JsonQuery().loadJson("{}").query(query)
        } catch (e: SyntaxException) {
            return e.message!!
        }
    }
}