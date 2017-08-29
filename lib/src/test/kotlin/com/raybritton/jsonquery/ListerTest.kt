package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.printer.list
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ListerTest {

    @Test
    fun testSimple() {
        //Given sample json and select all query
        val json1 = """[1,2,3,4]"""
        val json2 = """["a","b","c","d","e"]"""
        val gson = Gson()
        val query = Query(Query.Method.SELECT, ".")

        //When processed
        val result1 = gson.fromJson<Any>(json1, Any::class.java).list(query)
        val result2 = gson.fromJson<Any>(json2, Any::class.java).list(query)

        //Then check description is accurate
        assertEquals("json1", "[1.0, 2.0, 3.0, 4.0]", result1)
        assertEquals("json2", "[a, b, c, d, e]", result2)
    }

    @Test
    fun testWithKeys() {
        //Given sample json and queries for selecting parts with and without keys
        val json = """{"ids":["a","b","c"], "element":"value", "object":{"k1":"v1","k2":"v2"}}"""
        val jsonQuery = JsonQuery()
        val elementQuery = """SELECT ".element""""
        val elementKeysQuery = """SELECT ".element" WITH KEYS"""
        val objectQuery = """SELECT ".object""""
        val objectKeysQuery = """SELECT ".object" WITH KEYS"""
        val objectElementQuery = """SELECT "k1" FROM ".object""""
        val objectElementKeysQuery = """SELECT "k1" FROM ".object" WITH KEYS"""
        val objectTwoElementQuery = """SELECT ("k1", "k2") FROM ".object""""
        val objectTwoElementKeysQuery = """SELECT ("k1", "k2") FROM ".object" WITH KEYS"""

        //When processed
        jsonQuery.loadJson(json)
        val elementResult = jsonQuery.query(elementQuery)
        val elementKeysResult = jsonQuery.query(elementKeysQuery)
        val objectResult = jsonQuery.query(objectQuery)
        val objectKeysResult = jsonQuery.query(objectKeysQuery)
        val objectElementResult = jsonQuery.query(objectElementQuery)
        val objectElementKeysResult = jsonQuery.query(objectElementKeysQuery)
        val objectTwoElementResult = jsonQuery.query(objectTwoElementQuery)
        val objectTwoElementKeysResult = jsonQuery.query(objectTwoElementKeysQuery)

        //Then check output is accurate
        assertEquals("element", "value", elementResult)
        assertEquals("element keys", "element: value", elementKeysResult)
        assertEquals("object", "{v1, v2}", objectResult)
        assertEquals("object keys", "{k1: v1, k2: v2}", objectKeysResult)
        assertEquals("object element", "v1", objectElementResult)
        assertEquals("object element keys", "k1: v1", objectElementKeysResult)
        assertEquals("object two element", "{v1, v2}", objectTwoElementResult)
        assertEquals("object two element keys", "{k1: v1, k2: v2}", objectTwoElementKeysResult)
    }
}