package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class ArraySelectionTests {
    @Test
    fun `test selecting one element`() {
        val json = """[1,2]"""
        val result = JsonQuery(json).query("""SELECT '[0]' FROM '.'""")

        assertEquals("1.0", result)
    }
}