package com.raybritton.jsonquery.unit

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigationTests {
    @Test
    fun testNoNav() {
        val json = "{'a':1,'inner':{'a':2, 'deep': {'a':3}}}"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.'")

        assertEquals("Output", "1.0", output)
    }

    @Test
    fun testOneLayerNav() {
        val json = "{'a':1,'inner':{'a':2, 'deep': {'a':3}}}"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.inner'")

        assertEquals("Output", "2.0", output)
    }

    @Test
    fun testTwoLayerNav() {
        val json = "{'a':1,'inner':{'a':2, 'deep': {'a':3}}}"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.inner.deep'")

        assertEquals("Output", "3.0", output)
    }

    @Test
    fun testObjectInListNav() {
        val json = "{'list':[{'a':10},{'a':11},{'a':12}]}"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.list'")

        assertEquals("Output", "[10.0, 11.0, 12.0]", output)
    }

    @Test
    fun testObjectInObjectInListNav() {
        val json = "{'list':[{'inner':{'a':50}},{'inner':{'a':51}},{'inner':{'b':49}},{'inner':{'a':null}}]}"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.list.inner'")

        assertEquals("Output", "[50.0, 51.0]", output)
    }

    @Test
    fun testMulitpleValuesInObjectInObjectInListNav() {
        val json = "{'list':[{'inner':{'a':50, 'b': 'test'}},{'inner':{'a':51}},{'inner':{'b':49}},{'inner':{'a':null}}]}"

        val output = JsonQuery().loadJson(json).query("SELECT ('a','b') FROM '.list.inner'")

        assertEquals("Output", "[{50.0, \"test\"}, {51.0}, {49.0}]", output)
    }

    @Test
    fun testMulitpleValuesInObjectInObjectInListWithKeysNav() {
        val json = "{'list':[{'inner':{'a':50, 'b': 'test'}},{'inner':{'a':51}},{'inner':{'b':49}},{'inner':{'a':null}}]}"

        val output = JsonQuery().loadJson(json).query("SELECT ('a','b') FROM '.list.inner' WITH KEYS")

        assertEquals("Output", "[{a: 50.0, b: \"test\"}, {a: 51.0}, {b: 49.0}]", output)
    }

    @Test
    fun testObjectInObjectInListAtIndexNav() {
        val json = "{'list':[{'inner':{'a':50}},{'inner':{'a':51}},{'inner':{'b':49}},{'inner':{'a':null}}]}"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.list[1].inner'")

        assertEquals("Output", "51.0", output)
    }

    @Test
    fun testObjectInObjectInListAtIndexAtIndexNav() {
        val json = "[{'list':[{'inner':{'a':'here'}}]},{'list':[{'inner':{'a':'not here'}}]}]"

        val output = JsonQuery().loadJson(json).query("SELECT 'a' FROM '.[0].list[0].inner'")

        assertEquals("Output", "\"here\"", output)
    }
}