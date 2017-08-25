package com.raybritton.jsonquery

import com.raybritton.jsonquery.models.Query
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QueryParserTest {

    @Test
    fun testSimple() {
        //Given sample queries
        val queryStmt1 = "DESCRIBE \".\""
        val queryStmt2 = "DESCRIBE      \".items\""
        val queryStmt3 = "LIST VALUES \".items.id\""
        val queryStmt4 = "LIST \".items.id\""
        val queryStmt5 = "GET \".items.id\""

        //When processed
        val query1 = queryStmt1.toQuery()
        val query2 = queryStmt2.toQuery()
        val query3 = queryStmt3.toQuery()
        val query4 = queryStmt4.toQuery()
        val query5 = queryStmt5.toQuery()

        //Then check it matches
        assertEquals("query1 method", Query.Method.DESCRIBE, query1.method)
        assertEquals("query1 target", ".", query1.target)
        assertEquals("query1 isjson", false, query1.asJson)
        assertEquals("query1 withKeys", false, query1.withKeys)
        assertEquals("query1 skip", 0, query1.skip)
        assertEquals("query1 limit", 0, query1.limit)
        assertEquals("query1 where", null, query1.where)

        assertEquals("query2 method", Query.Method.DESCRIBE, query2.method)
        assertEquals("query2 target", ".items", query2.target)
        assertEquals("query2 isjson", false, query2.asJson)
        assertEquals("query2 withKeys", false, query2.withKeys)
        assertEquals("query2 skip", 0, query2.skip)
        assertEquals("query2 limit", 0, query2.limit)
        assertEquals("query2 where", null, query2.where)

        assertEquals("query3 method", Query.Method.LIST, query3.method)
        assertEquals("query3 target", "VALUES .items.id", query3.target)
        assertEquals("query3 isjson", false, query3.asJson)
        assertEquals("query3 withKeys", false, query3.withKeys)
        assertEquals("query3 skip", 0, query3.skip)
        assertEquals("query3 limit", 0, query3.limit)
        assertEquals("query3 where", null, query3.where)

        assertEquals("query4 method", Query.Method.LIST, query4.method)
        assertEquals("query4 target", ".items.id", query4.target)
        assertEquals("query4 isjson", false, query4.asJson)
        assertEquals("query4 withKeys", false, query4.withKeys)
        assertEquals("query4 skip", 0, query4.skip)
        assertEquals("query4 limit", 0, query4.limit)
        assertEquals("query4 where", null, query4.where)

        assertEquals("query5 method", Query.Method.GET, query5.method)
        assertEquals("query5 target", ".items.id", query5.target)
        assertEquals("query5 isjson", false, query5.asJson)
        assertEquals("query5 withKeys", false, query5.withKeys)
        assertEquals("query5 skip", 0, query5.skip)
        assertEquals("query5 limit", 0, query5.limit)
        assertEquals("query5 where", null, query5.where)
    }

    @Test
    fun testSkipLimit() {
        //Given sample queries
        val skip = "GET \".id\" SKIP 1"
        val limit = "LIST \".id\" LIMIT 9"
        val both = "LIST \".id\" SKIP 3 LIMIT 4"
        val bothRev = "LIST \".id\" LIMIT 2 SKIP 3"

        //When processed
        val resultSkip = skip.toQuery()
        val resultLimit = limit.toQuery()
        val resultBoth = both.toQuery()
        val resultBothRev = bothRev.toQuery()

        //Then check it matches
        assertEquals("skip method", Query.Method.GET, resultSkip.method)
        assertEquals("skip target", ".id", resultSkip.target)
        assertEquals("skip isjson", false, resultSkip.asJson)
        assertEquals("skip withKeys", false, resultSkip.withKeys)
        assertEquals("skip skip", 1, resultSkip.skip)
        assertEquals("skip limit", 0, resultSkip.limit)
        assertEquals("skip where", null, resultSkip.where)

        assertEquals("limit method", Query.Method.LIST, resultLimit.method)
        assertEquals("limit target", ".id", resultLimit.target)
        assertEquals("limit isjson", false, resultLimit.asJson)
        assertEquals("limit withKeys", false, resultLimit.withKeys)
        assertEquals("limit skip", 0, resultLimit.skip)
        assertEquals("limit limit", 9, resultLimit.limit)
        assertEquals("limit where", null, resultLimit.where)

        assertEquals("both method", Query.Method.LIST, resultBoth.method)
        assertEquals("both target", ".id", resultBoth.target)
        assertEquals("both isjson", false, resultBoth.asJson)
        assertEquals("both withKeys", false, resultBoth.withKeys)
        assertEquals("both skip", 3, resultBoth.skip)
        assertEquals("both limit", 4, resultBoth.limit)
        assertEquals("both where", null, resultBoth.where)

        assertEquals("bothrev method", Query.Method.LIST, resultBothRev.method)
        assertEquals("bothrev target", ".id", resultBothRev.target)
        assertEquals("bothrev isjson", false, resultBothRev.asJson)
        assertEquals("bothrev withKeys", false, resultBothRev.withKeys)
        assertEquals("bothrev skip", 3, resultBothRev.skip)
        assertEquals("bothrev limit", 2, resultBothRev.limit)
        assertEquals("bothrev where", null, resultBothRev.where)
    }

    @Test
    fun testFullStatement() {
        //Given a full query
        val query = "LIST VALUES \".items.id\" WHERE \".items.title\" == \"Hello\" SKIP 1 LIMIT 10 AS JSON WITH KEYS"

        //When processed
        val result = query.toQuery()

        //Then it's correct
        assertEquals("method", Query.Method.LIST, result.method)
        assertEquals("target", "VALUES .items.id", result.target)
        assertEquals("where target", ".items.title", result.where!!.target)
        assertEquals("where operator", Query.Where.Operator.EQUAL, result.where.operator)
        assertEquals("where compare", "Hello", result.where.compare)
        assertEquals("skip count", 1, result.skip)
        assertEquals("limit count", 10, result.limit)
        assertEquals("as json", true, result.asJson)
        assertEquals("with keys", true, result.withKeys)
    }
}