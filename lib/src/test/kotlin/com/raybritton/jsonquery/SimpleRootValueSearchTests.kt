package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleRootValueSearchTests {
    @Test
    fun testLowerStringArray() {
        val json = "['a','b','c']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'a'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'b'")

        assertEquals("Search 1", ".[0]", output1)
        assertEquals("Search 2", ".[1]", output2)
    }

    @Test
    fun testUpperStringArray() {
        val json = "['A','B','C']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'A'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'B'")

        assertEquals("Search 1", ".[0]", output1)
        assertEquals("Search 2", ".[1]", output2)
    }

    @Test
    fun testMixedStringArray() {
        val json = "['a','b','c', 'A', 'B', 'C', 'Word', 'word', 'worD']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'a'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'B'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'word'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'Word'")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'word' CASE SENSITIVE")
        val output6 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'Word' CASE SENSITIVE")

        assertEquals("Search 1", ".[0]\n.[3]", output1)
        assertEquals("Search 2", ".[1]\n.[4]", output2)
        assertEquals("Search 3", ".[6]\n.[7]\n.[8]", output3)
        assertEquals("Search 4", ".[6]\n.[7]\n.[8]", output4)
        assertEquals("Search 5", ".[7]", output5)
        assertEquals("Search 6", ".[6]", output6)
    }

    @Test
    fun testIntegerArray() {
        val json = "[1,2,3]"

        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1'")

        assertEquals("Search 1", ".[0]", output2)
    }

    @Test
    fun testDoubleArray() {
        val json = "[1.0,2.2,3.1542341]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1.0'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '2.2'")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '3.1542341'")

        assertEquals("Search 1", ".[0]", output1)
        assertEquals("Search 2", ".[0]", output2)
        assertEquals("Search 4", ".[1]", output4)
        assertEquals("Search 5", ".[2]", output5)
    }

    @Test
    fun testMixedNumberArray() {
        val json = "[1,2,3.78]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '3.78'")

        assertEquals("Search 1", ".[0]", output1)
        assertEquals("Search 2", ".[2]", output2)
    }

    @Test
    fun testBooleanArray() {
        val json = "[true, false, true]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'true'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'false'")

        assertEquals("Search 1", ".[0]\n.[2]", output1)
        assertEquals("Search 2", ".[1]", output2)
    }

    @Test
    fun testStringAndBooleanArray() {
        val json = "['first',true,'true','fourth']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'true'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'fourth'")

        assertEquals("Search 1", ".[1]\n.[2]", output1)
        assertEquals("Search 2", ".[3]", output2)
    }

    @Test
    fun testStringAndNumberArray() {
        val json = "[10,4.343,'test']"

        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '10'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '10.0'")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '4.343'")
        val output6 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'test'")

        assertEquals("Search 2", ".[0]", output2)
        assertEquals("Search 3", ".[0]", output3)
        assertEquals("Search 5", ".[1]", output5)
        assertEquals("Search 6", ".[2]", output6)
    }

    @Test
    fun testStringNumberAndBooleanArray() {
        val json = "[45,6.78,true,'word','45']"

        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '45'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '6.78'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'true'")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'word'")

        assertEquals("Search 2", ".[0]\n.[4]", output2)
        assertEquals("Search 3", ".[1]", output3)
        assertEquals("Search 4", ".[2]", output4)
        assertEquals("Search 5", ".[3]", output5)
    }

    @Test
    fun testLowerStringArrayWithValues() {
        val json = "['a','b','c']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'a' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'b' WITH VALUES")

        assertEquals("Search 1", ".[0]: a", output1)
        assertEquals("Search 2", ".[1]: b", output2)
    }

    @Test
    fun testUpperStringArrayWithValues() {
        val json = "['A','B','C']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'A' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'B' WITH VALUES")

        assertEquals("Search 1", ".[0]: A", output1)
        assertEquals("Search 2", ".[1]: B", output2)
    }

    @Test
    fun testMixedStringArrayWithValues() {
        val json = "['a','b','c', 'A', 'B', 'C', 'Word', 'word', 'worD']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'a' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'B' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'word' WITH VALUES")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'Word' WITH VALUES")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'word' CASE SENSITIVE WITH VALUES")
        val output6 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'Word' CASE SENSITIVE WITH VALUES")

        assertEquals("Search 1", ".[0]: a\n.[3]: A", output1)
        assertEquals("Search 2", ".[1]: b\n.[4]: B", output2)
        assertEquals("Search 3", ".[6]: Word\n.[7]: word\n.[8]: worD", output3)
        assertEquals("Search 4", ".[6]: Word\n.[7]: word\n.[8]: worD", output4)
        assertEquals("Search 5", ".[7]: word", output5)
        assertEquals("Search 6", ".[6]: Word", output6)
    }

    @Test
    fun testIntegerArrayWithValues() {
        val json = "[1,2,3]"

        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1' WITH VALUES")

        assertEquals("Search 1", ".[0]: 1.0", output2)
    }

    @Test
    fun testDoubleArrayWithValues() {
        val json = "[1.0,2.2,3.1542341]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1.0' WITH VALUES")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '2.2' WITH VALUES")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '3.1542341' WITH VALUES")

        assertEquals("Search 1", ".[0]: 1.0", output1)
        assertEquals("Search 2", ".[0]: 1.0", output2)
        assertEquals("Search 4", ".[1]: 2.2", output4)
        assertEquals("Search 5", ".[2]: 3.1542341", output5)
    }

    @Test
    fun testMixedNumberArrayWithValues() {
        val json = "[1,2,3.78]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '3.78' WITH VALUES")

        assertEquals("Search 1", ".[0]: 1.0", output1)
        assertEquals("Search 2", ".[2]: 3.78", output2)
    }

    @Test
    fun testBooleanArrayWithValues() {
        val json = "[true, false, true]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'true' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'false' WITH VALUES")

        assertEquals("Search 1", ".[0]: true\n.[2]: true", output1)
        assertEquals("Search 2", ".[1]: false", output2)
    }

    @Test
    fun testStringAndBooleanArrayWithValues() {
        val json = "['first',true,'true','fourth']"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'true' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'fourth' WITH VALUES")

        assertEquals("Search 1", ".[1]: true\n.[2]: true", output1)
        assertEquals("Search 2", ".[3]: fourth", output2)
    }

    @Test
    fun testStringAndNumberArrayWithValues() {
        val json = "[10,4.343,'test']"

        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '10' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '10.0' WITH VALUES")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '4.343' WITH VALUES")
        val output6 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'test' WITH VALUES")

        assertEquals("Search 2", ".[0]: 10.0", output2)
        assertEquals("Search 3", ".[0]: 10.0", output3)
        assertEquals("Search 5", ".[1]: 4.343", output5)
        assertEquals("Search 6", ".[2]: test", output6)
    }

    @Test
    fun testStringNumberAndBooleanArrayWithValues() {
        val json = "[45,6.78,true,'word','45']"

        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '45' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '6.78' WITH VALUES")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'true' WITH VALUES")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'word' WITH VALUES")

        assertEquals("Search 2", ".[0]: 45.0\n.[4]: 45", output2)
        assertEquals("Search 3", ".[1]: 6.78", output3)
        assertEquals("Search 4", ".[2]: true", output4)
        assertEquals("Search 5", ".[3]: word", output5)
    }

}