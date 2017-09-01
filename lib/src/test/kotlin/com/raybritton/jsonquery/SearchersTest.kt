package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchersTest {

    @Test
    fun testSimple() {
        //Given simple json
        val json1 = """{"key1":"value1"}"""

        //When searched both ways
        val query1 = "SEARCH \".\" FOR KEY \"key1\""
        val query2 = "SEARCH \".\" FOR VALUE \"value1\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result1 = jsonQuery.query(query1)
        val result2 = jsonQuery.query(query2)

        //Then check both results are correct (both complete and the same)
        assertEquals("result 1", ".key1: value1", result1)
        assertEquals("result 2", ".key1: value1", result2)
    }

    @Test
    fun testArray() {
        //Given simple array jsons
        val json1 = """["a","b","c"]"""
        val json2 = """[1,2,3]"""

        //When s
        val query1 = "SEARCH \".\" FOR VALUE \"b\""
        val query2 = "SEARCH \".\" FOR VALUE \"2\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result1 = jsonQuery.query(query1)
        jsonQuery.loadJson(json2)
        val result2 = jsonQuery.query(query2)

        //Then check results are correct (array notation)
        assertEquals("result 1", ".[1]: b", result1)
        assertEquals("result 2", ".[1]: 2.0", result2)
    }

    @Test
    fun testInnerObject() {
        //Given simple json
        val json1 = """{"inner":{"key1":"value1"}}"""

        //When searched both ways
        val query = "SEARCH \".\" FOR VALUE \"value1\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result = jsonQuery.query(query)

        //Then check both results are as expected
        assertEquals("result", ".inner.key1: value1", result)
    }

    @Test
    fun testRestrictedSearch() {
        //Given simple json
        val json1 = """{"obj1":{"k1":"v"}, "obj2":{"k2":"v"}}"""

        //When searched in one object for value
        val query = "SEARCH \".obj1\" FOR VALUE \"v\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result = jsonQuery.query(query)

        //Then check both results are as expected
        assertEquals("result", ".obj1.k1: v", result)
    }

    @Test
    fun testMutlipleResults() {
        //Given simple json
        val json1 = """[{"k1":"v"},{"k2":"v"},{"k3":"v"}]"""

        //When searched both ways
        val query = "SEARCH \".\" FOR VALUE \"v\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result = jsonQuery.query(query)

        //Then check both results are as expected
        assertEquals("result", ".[0].k1: v\n.[1].k2: v\n.[2].k3: v", result)
    }
}