package com.raybritton.jsonquery.unit

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class ByElementTests {
    @Test
    fun `test by element`() {
        val json = """[{a:[1,2]},{a:[5,1]},{b:90},true]"""
        val result1 = JsonQuery(json).query("""SELECT SUM('a') FROM '.'""")
        val result2 = JsonQuery(json).query("""SELECT SUM('a') FROM '.' BY ELEMENT""")

        assertEquals("9.0", result1)
        assertEquals("[3.0, 6.0]", result2)
    }

}