package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.models.ElementFieldProjection
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.SelectProjection
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.query.buildQuery
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.parsing.tokens.Token
import com.raybritton.jsonquery.parsing.tokens.toQueryTokens
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class QueryParserTests {
    @Test
    fun `test simple target`() {
        val queryStr = "SELECT \".\""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Token count", 2, tokens.size)
        assertEquals("Method token", Token.KEYWORD(Keyword.SELECT, 0), tokens[0])
        assertEquals("String token", Token.STRING(".", 0), tokens[1])
        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())
        assertEquals("Method", Query.Method.SELECT, query.method)
        assertEquals("Target type", Target.TargetField::class.java, query.target::class.java)
        assertEquals("Target", ".", (query.target as Target.TargetField).value)
    }

    @Test
    fun `test simple target with projection`() {
        val queryStr = "SELECT MIN(ELEMENT) FROM \".\""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Token count", 7, tokens.size)
        assertEquals("Method token", Token.KEYWORD(Keyword.SELECT, 0), tokens[0])
        assertEquals("Keyword(MIN) token", Token.KEYWORD(Keyword.MIN, 0), tokens[1])
        assertEquals("Punctuation '(' token", Token.PUNCTUATION('(', 0), tokens[2])
        assertEquals("Keyword(ELEMENT) token", Token.KEYWORD(Keyword.ELEMENT, 0), tokens[3])
        assertEquals("Punctuation ')' token", Token.PUNCTUATION(')', 0), tokens[4])
        assertEquals("Keyword(FROM) token", Token.KEYWORD(Keyword.FROM, 0), tokens[5])
        assertEquals("String token", Token.STRING(".", 0), tokens[6])
        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())
        assertEquals("Method", Query.Method.SELECT, query.method)
        assertNotNull("Select projection", query.select?.projection)
        assertEquals("Projection type", SelectProjection.Math::class.java, query.select!!.projection!!::class.java)
        assertEquals("Projection math", Keyword.MIN, (query.select.projection as SelectProjection.Math).expr)
        assertEquals("Projection field", ElementFieldProjection.Element, query.select.projection.field)
        assertEquals("Target type", Target.TargetField::class.java, query.target::class.java)
        assertEquals("Target", ".", (query.target as Target.TargetField).value)
    }
}