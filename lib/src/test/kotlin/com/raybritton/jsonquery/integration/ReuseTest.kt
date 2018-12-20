package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ReuseTest {
    @Test
    fun testSimpleNoReuse() {
        val json = "[{'a':10, 'c': 'word', 'alpha':89},{'a':20, 'c':'word', 'beta': '234'}]"

        val output1 = JsonQuery().loadJson(json).query("SELECT KEYS FROM '.'")
        val output2 = JsonQuery().loadJson(json).query("SELECT VALUES FROM '.'")
        val output3 = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.'")
        val output4 = JsonQuery().loadJson(json).query("SELECT DISTINCT KEYS FROM '.' WHERE 'a' == 10")
        val output5 = JsonQuery().loadJson(json).query("SELECT DISTINCT KEYS FROM '.'")
        val output6 = JsonQuery().loadJson(json).query("SELECT DISTINCT VALUES FROM '.'")

        assertEquals("Output 1", "[\"a\", \"c\", \"alpha\", \"a\", \"c\", \"beta\"]", output1)
        assertEquals("Output 2", "[10.0, \"word\", 89.0, 20.0, \"word\", \"234\"]", output2)
        assertEquals("Output 3", "[10.0, 20.0]", output3)
        assertEquals("Output 4", "[\"a\", \"c\", \"alpha\"]", output4)
        assertEquals("Output 5", "[\"a\", \"c\", \"alpha\", \"beta\"]", output5)
        assertEquals("Output 6", "[10.0, \"word\", 89.0, 20.0, \"234\"]", output6)
    }

    @Test
    fun testSimpleWithReuse() {
        val json = "{'a':10, 'c': 'word', 'alpha':89}"

        val jsonQuery = JsonQuery().loadJson(json)

        val output1 = jsonQuery.query("SELECT KEYS FROM '.'")
        val output2 = jsonQuery.query("SELECT VALUES FROM '.'")
        val output3 = jsonQuery.query("SELECT 'a' FROM '.'")
        val output4 = jsonQuery.query("SELECT KEYS FROM '.' WHERE 'a' == 10")

        assertEquals("Output 1", "[\"a\", \"c\", \"alpha\"]", output1)
        assertEquals("Output 2", "[10.0, \"word\", 89.0]", output2)
        assertEquals("Output 3", "10.0", output3)
        assertEquals("Output 4", "[\"a\", \"c\", \"alpha\"]", output4)
    }
}