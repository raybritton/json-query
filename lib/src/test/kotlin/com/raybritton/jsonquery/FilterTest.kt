package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FilterTest {
    @Test
    fun testDistinct() {
        //Given json arraies with multiple repeated elements
        val json1 = """[1,2,3,4,5,6,1,2,3,4,5,4,3,2,6,7,34,0]"""
        val json2 = """["a", "b", "ab", "a"]"""

        //When queried using order by and distinct
        val query = "SELECT DISTINCT \".\" ORDER BY ELEMENT"
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result1 = jsonQuery.query(query)
        jsonQuery.loadJson(json2)
        val result2 = jsonQuery.query(query)

        //Then check the results are unique and ordered
        assertEquals("result 1", "[0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 34.0]", result1)
        assertEquals("result 2", """[a, ab, b]""", result2)
    }
}