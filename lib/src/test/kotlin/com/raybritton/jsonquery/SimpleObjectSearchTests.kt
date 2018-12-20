package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleObjectSearchTests {
    @Test
    fun testKeyInRoot() {
        val json = "{'a':1,'b':2,'code':'435','delta':'b'}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'a'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'cod'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'code'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'b'")

        assertEquals("Search 1", ".a", output1)
        assertEquals("Search 2", "[]", output2)
        assertEquals("Search 3", ".code", output3)
        assertEquals("Search 4", ".b", output4)
    }

    @Test
    fun testValuesInRoot() {
        val json = "{'a':1,'b':2,'code':'435','delta':'b'}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '43'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '435'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'b'")

        assertEquals("Search 1", ".a", output1)
        assertEquals("Search 2", "[]", output2)
        assertEquals("Search 3", ".code", output3)
        assertEquals("Search 4", ".delta", output4)
    }

    @Test
    fun testBothInRoot() {
        val json = "{'a':1,'b':2,'code':'435','delta':'b'}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'a'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'b'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR '435'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'del'")

        assertEquals("Search 1", ".a", output1)
        assertEquals("Search 2", ".b\n.delta", output2)
        assertEquals("Search 3", ".code", output3)
        assertEquals("Search 4", "[]", output4)
    }

    @Test
    fun testKeyInRootWithValues() {
        val json = "{'a':1,'b':2,'code':'435','delta':'b'}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'a' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'cod' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'code' WITH VALUES")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'b' WITH VALUES")

        assertEquals("Search 1", ".a: 1.0", output1)
        assertEquals("Search 2", "[]", output2)
        assertEquals("Search 3", ".code: 435", output3)
        assertEquals("Search 4", ".b: 2.0", output4)
    }

    @Test
    fun testValuesInRootWithValues() {
        val json = "{'a':1,'b':2,'code':'435','delta':'b'}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '1' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '43' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '435' WITH VALUES")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE 'b' WITH VALUES")

        assertEquals("Search 1", ".a: 1.0", output1)
        assertEquals("Search 2", "[]", output2)
        assertEquals("Search 3", ".code: 435", output3)
        assertEquals("Search 4", ".delta: b", output4)
    }

    @Test
    fun testBothInRootWithValues() {
        val json = "{'a':1,'b':2,'code':'435','delta':'b'}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'a' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'b' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR '435' WITH VALUES")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'del' WITH VALUES")

        assertEquals("Search 1", ".a: 1.0", output1)
        assertEquals("Search 2", ".b: 2.0\n.delta: b", output2)
        assertEquals("Search 3", ".code: 435", output3)
        assertEquals("Search 4", "[]", output4)
    }

    @Test
    fun testKeysInInner() {
        val json = "{'in':{'x':'yok','a':true,'pop':100}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR KEY 'x'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR KEY 'pop'")

        assertEquals("Search 1", ".in.x", output1)
        assertEquals("Search 2", ".in.pop", output2)
    }

    @Test
    fun testValuesInInner() {
        val json = "{'in':{'x':'yok','a':true,'pop':100}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR VALUE 'yok'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR VALUE '100'")

        assertEquals("Search 1", ".in.x", output1)
        assertEquals("Search 2", ".in.pop", output2)
    }

    @Test
    fun testBothInInner() {
        val json = "{'in':{'x':'yok','a':true,'pop':100}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR 'x'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR 'pop'")

        assertEquals("Search 1", ".in.x", output1)
        assertEquals("Search 2", ".in.pop", output2)
    }

    @Test
    fun testKeysInInnerWithValues() {
        val json = "{'in':{'x':'yok','a':true,'pop':100}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR KEY 'x' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR KEY 'pop' WITH VALUES")

        assertEquals("Search 1", ".in.x: yok", output1)
        assertEquals("Search 2", ".in.pop: 100.0", output2)
    }

    @Test
    fun testValuesInInnerWithValues() {
        val json = "{'in':{'x':'yok','a':true,'pop':100}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR VALUE 'yok' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR VALUE '100' WITH VALUES")

        assertEquals("Search 1", ".in.x: yok", output1)
        assertEquals("Search 2", ".in.pop: 100.0", output2)
    }

    @Test
    fun testBothInInnerWithValues() {
        val json = "{'in':{'x':'yok','a':true,'pop':100}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR 'x' WITH VALUES")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.in' FOR 'pop' WITH VALUES")

        assertEquals("Search 1", ".in.x: yok", output1)
        assertEquals("Search 2", ".in.pop: 100.0", output2)
    }
}