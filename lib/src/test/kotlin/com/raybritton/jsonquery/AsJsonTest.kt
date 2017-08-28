package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AsJsonTest {

    @Test
    fun testSimple() {
        //Given json to be converted
        val origJson1 = """{"key1":"value1"}"""
        val origJson2 = """{"key1":"value1","key2":"value2"}"""
        val origJson3 = """["a","b","c"]"""
        val origJson4 = """{"key1":"value1","key2":[1.0,2.0,3.0,4.0]}"""

        //When converted to obj and back to json
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(origJson1)
        val result1 = jsonQuery.query("SELECT \".\" AS JSON")
        jsonQuery.loadJson(origJson2)
        val result2 = jsonQuery.query("SELECT \".\" AS JSON")
        jsonQuery.loadJson(origJson3)
        val result3 = jsonQuery.query("SELECT \".\" AS JSON")
        jsonQuery.loadJson(origJson4)
        val result4 = jsonQuery.query("SELECT \".\" AS JSON")

        //Then check it's as expected
        assertEquals("result 1", origJson1, result1)
        assertEquals("result 2", origJson2, result2)
        assertEquals("result 3", origJson3, result3)
        assertEquals("result 4", origJson4, result4)
    }
}