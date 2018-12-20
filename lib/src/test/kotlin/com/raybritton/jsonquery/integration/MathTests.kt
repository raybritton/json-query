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

    @Test
    fun testMaxSample() {
        val json1 = "[0, 1, 2]"
        val json2 = "[10, 100, 100]"
        val json3 = "[-10, 10]"
        val json4 = "['a', 'b']"
        val json5 = "[1, 'b']"
        val json6 = "[93423]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT MAX(ELEMENT) FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT MAX(ELEMENT) FROM '.'")
        val output3 = JsonQuery().loadJson(json3).query("SELECT MAX(ELEMENT) FROM '.'")
        val output4 = JsonQuery().loadJson(json4).query("SELECT MAX(ELEMENT) FROM '.'")
        val output5 = JsonQuery().loadJson(json5).query("SELECT MAX(ELEMENT) FROM '.'")
        val output6 = JsonQuery().loadJson(json6).query("SELECT MAX(ELEMENT) FROM '.'")

        assertEquals("Output 1", "2.0", output1)
        assertEquals("Output 2", "100.0", output2)
        assertEquals("Output 3", "10.0", output3)
        assertEquals("Output 4", "NaN", output4)
        assertEquals("Output 5", "1.0", output5)
        assertEquals("Output 6", "93423.0", output6)
    }

    @Test
    fun testMaxNested() {
        val json1 = "[{'list':[10]}]"
        val json2 = "[{'list':[50]},{'list':[11]},{'list':[14]}]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT MAX('.list') FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT MAX('.list') FROM '.'")

        assertEquals("Output 1", "10.0", output1)
        assertEquals("Output 2", "50.0", output2)
    }

    @Test
    fun testSumSample() {
        val json1 = "[0, 1, 2]"
        val json2 = "[10, 100, 100]"
        val json3 = "[-10, 10]"
        val json4 = "['a', 'b']"
        val json5 = "[1, 'b']"
        val json6 = "[93423]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT SUM(ELEMENT) FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT SUM(ELEMENT) FROM '.'")
        val output3 = JsonQuery().loadJson(json3).query("SELECT SUM(ELEMENT) FROM '.'")
        val output4 = JsonQuery().loadJson(json4).query("SELECT SUM(ELEMENT) FROM '.'")
        val output5 = JsonQuery().loadJson(json5).query("SELECT SUM(ELEMENT) FROM '.'")
        val output6 = JsonQuery().loadJson(json6).query("SELECT SUM(ELEMENT) FROM '.'")

        assertEquals("Output 1", "3.0", output1)
        assertEquals("Output 2", "210.0", output2)
        assertEquals("Output 3", "0.0", output3)
        assertEquals("Output 4", "0.0", output4)
        assertEquals("Output 5", "1.0", output5)
        assertEquals("Output 6", "93423.0", output6)
    }

    @Test
    fun testSumNested() {
        val json1 = "[{'list':[10]}]"
        val json2 = "[{'list':[50]},{'list':[11]},{'list':[14]}]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT SUM('.list') FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT SUM('.list') FROM '.'")

        assertEquals("Output 1", "10.0", output1)
        assertEquals("Output 2", "75.0", output2)
    }

    @Test
    fun testCountSample() {
        val json1 = "[0, 1, 2]"
        val json2 = "[]"
        val json5 = "{'list':[4,5,6]}"
        val json6 = "[93423]"

        val output1 = JsonQuery().loadJson(json1).query("SELECT COUNT(ELEMENT) FROM '.'")
        val output2 = JsonQuery().loadJson(json2).query("SELECT COUNT(ELEMENT) FROM '.'")
        val output6 = JsonQuery().loadJson(json6).query("SELECT COUNT(ELEMENT) FROM '.'")
        val output10 = JsonQuery().loadJson(json5).query("SELECT COUNT(ELEMENT) FROM '.list'")

        assertEquals("Output 1", "3", output1)
        assertEquals("Output 2", "0", output2)
        assertEquals("Output 6", "1", output6)
        assertEquals("Output 10", "3", output10)
    }

}