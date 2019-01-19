package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.query.buildQuery
import com.raybritton.jsonquery.parsing.tokens.toQueryTokens
import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleQueryParserTests {
    @Test
    fun `test long select`() {
        val queryStr = """SELECT DISTINCT ("foo.bar" AS "baz", "test.test", "foobar") FROM ".inner" WHERE "baz" == 12.3 LIMIT 3 OFFSET 4 ORDER BY "foo" AS JSON PRETTY"""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())
    }

    @Test
    fun `test triple nested query`() {
        val queryStr = """SELECT "foo" FROM (SELECT (SELECT "foo" FROM "." AS JSON) AS JSON)"""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())

        assertEquals("second query", "SELECT (SELECT \"foo\" FROM \".\" AS JSON) AS JSON", (query.target as Target.TargetQuery).query.toString().trim())
        assertEquals("third query", "SELECT \"foo\" FROM \".\" AS JSON", (query.target.query.target as Target.TargetQuery).query.toString().trim())
    }

    @Test
    fun `test select in describe`() {
        val queryStr = """DESCRIBE (SELECT "foo" FROM "." AS JSON)"""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())
    }
}