package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.parsing.tokens.CharParser
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.parsing.tokens.OperatorParser
import org.junit.Assert.*
import org.junit.Test

class OperatorParserTests {
    @Test
    fun `test can parse`() {
        val valid = "=!#<>"
        val invalid = "1234567890-@£$%^&*()_+[]{};'\\:\"|,./?`~qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

        for (char in valid) {
            assertTrue(char.toString(), OperatorParser.canParse(char))
        }

        for (char in invalid) {
            assertFalse(char.toString(), OperatorParser.canParse(char))
        }
    }

    @Test
    fun `test simple operator detection`() {
        val operatorStrings = listOf("==", "=", "!=", "<", ">", "#", "!#", "<=", ">=")
        val operators = listOf(Operator.Equal, Operator.Equal, Operator.NotEqual, Operator.LessThan, Operator.GreaterThan, Operator.Contains, Operator.NotContains, Operator.LessThanOrEqual, Operator.GreaterThanOrEqual)

        operatorStrings.forEachIndexed { idx, opString ->
            val reader = CharParser(opString)
            val result = OperatorParser.parse(reader)
            assertEquals(opString, operators[idx], result.value)
        }
    }
}