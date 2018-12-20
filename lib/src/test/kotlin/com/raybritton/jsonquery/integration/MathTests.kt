package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class MathTests {
    @Test
    fun testMinSimple() {
        val json1 = "[0, 1, 2]"
        val json2 = "[10, 100, 100]"
        val json3 = "[-10, 10]"
        val json4 = "['a', 'b']"
        val json5 = "[1, 'b']"
        val json6 = "[93423]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT MIN(ELEMENT) FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT MIN(ELEMENT) FROM '.'")
        val output3 = JsonQuery().loadJson(json3).query("SELECT MIN(ELEMENT) FROM '.'")
        val output4 = JsonQuery().loadJson(json4).query("SELECT MIN(ELEMENT) FROM '.'")
        val output5 = JsonQuery().loadJson(json5).query("SELECT MIN(ELEMENT) FROM '.'")
        val output6 = JsonQuery().loadJson(json6).query("SELECT MIN(ELEMENT) FROM '.'")

        assertEquals("Output 1", "0.0", output1)
        assertEquals("Output 2", "10.0", output2)
        assertEquals("Output 3", "-10.0", output3)
        assertEquals("Output 4", "NaN", output4)
        assertEquals("Output 5", "1.0", output5)
        assertEquals("Output 6", "93423.0", output6)
    }

    @Test
    fun testMinNested() {
        val json1 = "[{'list':[10]}]"
        val json2 = "[{'list':[50]},{'list':[11]},{'list':[14]}]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT MIN('.list') FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT MIN('.list') FROM '.'")

        assertEquals("Output 1", "10.0", output1)
        assertEquals("Output 2", "11.0", output2)
    }
}