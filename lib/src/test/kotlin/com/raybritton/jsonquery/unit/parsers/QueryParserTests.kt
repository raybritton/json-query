package com.raybritton.jsonquery.unit.parsers

import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.query.buildQuery
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.parsing.tokens.Token
import com.raybritton.jsonquery.parsing.tokens.toQueryTokens
import org.junit.Assert.*
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
        assertNotNull("Select", query.select)
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
        assertNotNull("Select", query.select)
        assertNotNull("Select projection", query.select?.projection)
        assertEquals("Projection type", SelectProjection.Math::class.java, query.select!!.projection!!::class.java)
        assertEquals("Projection math", Keyword.MIN, (query.select.projection as SelectProjection.Math).expr)
        assertEquals("Projection field", ElementFieldProjection.Element, query.select.projection.field)
        assertEquals("Target type", Target.TargetField::class.java, query.target::class.java)
        assertEquals("Target", ".", (query.target as Target.TargetField).value)
    }

    @Test
    fun `test select with where`() {
        val queryStr = "SELECT (\"person.firstName\", \"person.lastName\") FROM \".\" WHERE \"person.department\" # \"Foobar\" ORDER BY \"person.id\""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Token count", 15, tokens.size)
        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())
        assertEquals("Method token", Token.KEYWORD(Keyword.SELECT, 0), tokens[0])
        assertEquals("Multiple field start token", Token.PUNCTUATION('(', 0), tokens[1])
        assertEquals("Projection field 1 token", Token.STRING("person.firstName", 0), tokens[2])
        assertEquals("Multiple field separator token", Token.PUNCTUATION(',', 0), tokens[3])
        assertEquals("Projection field 2 token", Token.STRING("person.lastName", 0), tokens[4])
        assertEquals("Multiple field end token", Token.PUNCTUATION(')', 0), tokens[5])
        assertEquals("Keyword(FROM) token", Token.KEYWORD(Keyword.FROM, 0), tokens[6])
        assertEquals("Target token", Token.STRING(".", 0), tokens[7])
        assertEquals("Keyword(WHERE) token", Token.KEYWORD(Keyword.WHERE, 0), tokens[8])
        assertEquals("Where projection token", Token.STRING("person.department", 0), tokens[9])
        assertEquals("Where operator token", Token.OPERATOR(Operator.Contains, 0), tokens[10])
        assertEquals("Where compare token", Token.STRING("Foobar", 0), tokens[11])
        assertEquals("Keyword(ORDER) token", Token.KEYWORD(Keyword.ORDER, 0), tokens[12])
        assertEquals("Keyword(BY) token", Token.KEYWORD(Keyword.BY, 0), tokens[13])
        assertEquals("Order by target token", Token.STRING("person.id", 0), tokens[14])
        assertEquals("Method", Query.Method.SELECT, query.method)
        assertEquals("Target type", Target.TargetField::class.java, query.target::class.java)
        assertEquals("Target", ".", (query.target as Target.TargetField).value)
        assertNotNull("Where", query.where)
        assertNotNull("Select", query.select)
        assertEquals("Projection type", SelectProjection.MultipleFields::class.java, query.select!!.projection!!::class.java)
        assertEquals("Projection", listOf("person.firstName", "person.lastName"), (query.select.projection as SelectProjection.MultipleFields).fields)
        assertEquals("Order by type", ElementFieldProjection.Field::class.java, query.select.orderBy!!::class.java)
        assertEquals("Order by", "person.id", (query.select.orderBy as ElementFieldProjection.Field).value)
        assertEquals("Where operator", Operator.Contains, query.where!!.operator)
        assertEquals("Where projection type", WhereProjection.Field::class.java, query.where.projection::class.java)
        assertEquals("Where projection", "person.department", (query.where.projection as WhereProjection.Field).value)
        assertEquals("Where compare", "Foobar", query.where.value.value)
    }

    @Test
    fun `test simple nested query`() {
        val queryStr = "SELECT \"name\" FROM (SELECT \".\" AS JSON)"
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Token count", 9, tokens.size)
        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())
        assertEquals("Method token", Token.KEYWORD(Keyword.SELECT, 0), tokens[0])
        assertEquals("Target token", Token.STRING("name", 0), tokens[1])
        assertEquals("Keyword(FROM)", Token.KEYWORD(Keyword.FROM, 0), tokens[2])
        assertEquals("Inner query start", Token.PUNCTUATION('(', 0), tokens[3])
        assertEquals("Inner Method token", Token.KEYWORD(Keyword.SELECT, 0), tokens[4])
        assertEquals("Inner target", Token.STRING(".", 0), tokens[5])
        assertEquals("Keyword(AS)", Token.KEYWORD(Keyword.AS, 0), tokens[6])
        assertEquals("Keyword(JSON)", Token.KEYWORD(Keyword.JSON, 0), tokens[7])
        assertEquals("Inner query end", Token.PUNCTUATION(')', 0), tokens[8])

        val innerQuery = (query.target as Target.TargetQuery).query

        assertEquals("Method", Query.Method.SELECT, query.method)
        assertEquals("Target", innerQuery, query.target.query)
        assertEquals("Projection", "name", (query.select!!.projection as SelectProjection.SingleField).field)

        assertEquals("Inner Method", Query.Method.SELECT, innerQuery.method)
        assertEquals("Inner target", ".", (innerQuery.target as Target.TargetField).value)
        assertTrue("Inner as json", innerQuery.flags.isAsJson)
    }

    @Test
    fun `check every keyword for select`() {
        val queryStr = """SELECT DISTINCT "test" FROM "." BY ELEMENT WHERE "x" == "y" LIMIT 10 OFFSET 20 ORDER BY "id" AS JSON PRETTY"""
        val tokens = queryStr.toQueryTokens()
        val query = tokens.buildQuery(queryStr)

        assertEquals("Token count", 21, tokens.size)
        assertEquals("Original query", queryStr, query.originalString)
        assertEquals("Parsed query", queryStr, query.toString().trim())

        assertTrue("distinct", query.flags.isDistinct)
        assertTrue("as json", query.flags.isAsJson)
        assertTrue("pretty", query.flags.isPrettyPrinted)
        assertTrue("by element", query.flags.isByElement)
        assertEquals("limit", 10, query.select!!.limit)
        assertEquals("offset", 20, query.select.offset)
        assertEquals("order by", "id", (query.select.orderBy as ElementFieldProjection.Field).value)
    }
}