package com.raybritton.jsonquery

import com.raybritton.jsonquery.tools.toQuery
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QueryStringTest {

    @Test
    fun testSimple() {
        //Given query string
        val query1 = "DESCRIBE \".\""
        val query2 = """SELECT "id" FROM ".""""
        val query3 = """SEARCH "." FOR VALUE 12"""
        val query4 = """SELECT DISTINCT ("id", "title") FROM "." WHERE "peep" > 34.0 ORDER BY "name" DESC LIMIT 12 OFFSET 10 WITH KEYS AS JSON PRETTY"""

        //When converted to obj, back to string and to obj again
        val query1Obj = query1.toQuery()
        val query1Str = query1Obj.toString()
        val query1ObjAgain = query1Str.toQuery()
        val query2Obj = query2.toQuery()
        val query2Str = query2Obj.toString()
        val query2ObjAgain = query2Str.toQuery()
        val query3Obj = query3.toQuery()
        val query3Str = query3Obj.toString()
        val query3ObjAgain = query3Str.toQuery()
        val query4Obj = query4.toQuery()
        val query4Str = query4Obj.toString()
        val query4ObjAgain = query4Str.toQuery()

        //Then check all match
        assertEquals("query 1 str", query1, query1Str)
        assertEquals("query 1 obj", query1Obj, query1ObjAgain)
        assertEquals("query 2 str", query2, query2Str)
        assertEquals("query 2 obj", query2Obj, query2ObjAgain)
        assertEquals("query 3 str", query3, query3Str)
        assertEquals("query 3 obj", query3Obj, query3ObjAgain)
        assertEquals("query 4 str", query4, query4Str)
        assertEquals("query 4 obj", query4Obj, query4ObjAgain)
    }
}