package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MathTest {

    @Test
    fun testMax() {
        //Given
        val json1 = """[1,6,7,1]"""
        val json2 = """["a", "b"]"""
        val json3 = """[{"key":3},{"key":50}]"""

        //When
        val query1 = "SELECT MAX(ELEMENT) FROM \".\""
        val query2 = "SELECT MAX(\".key\") FROM \".\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result1 = jsonQuery.query(query1)
        jsonQuery.loadJson(json2)
        val result2 = jsonQuery.query(query1)
        jsonQuery.loadJson(json3)
        val result3 = jsonQuery.query(query2)

        //Then
        assertEquals("result 1", "7.0", result1)
        assertEquals("result 2", "NaN", result2)
        assertEquals("result 3", "50.0", result3)
    }

    @Test
    fun testMin() {
        //Given
        val json = """[5,1,2]"""

        //When
        val query = "SELECT MIN(ELEMENT) FROM \".\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result = jsonQuery.query(query)

        //Then
        assertEquals("result", "1.0", result)
    }

    @Test
    fun testSum() {
        //Given
        val json = """[1,2,3]"""

        //When
        val query = "SELECT SUM(ELEMENT) FROM \".\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result = jsonQuery.query(query)

        //Then
        assertEquals("result", "6.0", result)
    }

    @Test
    fun testCount() {
        //Given
        val json = """[5,2,6,3]"""

        //When
        val query = "SELECT COUNT(ELEMENT) FROM \".\""
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result = jsonQuery.query(query)

        //Then
        assertEquals("result", "4", result)
    }
}