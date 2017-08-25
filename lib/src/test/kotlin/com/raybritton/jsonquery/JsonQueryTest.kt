package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JsonQueryTest {

    @Test
    fun testSimple() {
        //Given sample json
        val json1 = """{"key1":"value1"}"""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result1 = jsonQuery.query("DESCRIBE \".\"")

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING)", result1)
    }
}
