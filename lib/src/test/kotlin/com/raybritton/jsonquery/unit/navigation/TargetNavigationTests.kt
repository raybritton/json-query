package com.raybritton.jsonquery.unit.navigation

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.tools.navigateToTarget
import org.junit.Assert.assertEquals
import org.junit.Test

class TargetNavigationTests {
    @Test
    fun `test navigating to the root object`() {
        val obj = JsonObject("foo" to 1)

        val result = obj.navigateToTarget("")

        assertEquals("No nav in obj", obj, result)
    }

    @Test
    fun `test navigating to the root array`() {
        val arr = JsonArray(1, 2)

        val result = arr.navigateToTarget("")

        assertEquals("No nav in arr", arr, result)
    }

    @Test
    fun `test navigating to object in root object`() {
        val inner = JsonObject("bar" to "test")
        val obj = JsonObject("foo" to inner)

        val result = obj.navigateToTarget(".foo")

        assertEquals("Obj in obj", inner, result)
    }

    @Test
    fun `test navigating to object in in object in root object`() {
        val deep = JsonObject("baz" to "test")
        val inner = JsonObject("bar" to deep)
        val obj = JsonObject("foo" to inner)

        val result = obj.navigateToTarget(".foo.bar")

        assertEquals("Obj in obj", deep, result)
    }

    @Test
    fun `test accessing deeply nested field in objects and arrays`() {
        val emptyObj = JsonObject()
        val layer8 = JsonObject("field" to 1)
        val layer7 = JsonArray(null, 1, layer8, emptyObj)
        val layer6 = JsonObject("red" to "herring", "array" to layer7)
        val layer5 = JsonObject("  " to layer6)
        val layer4 = JsonArray(1, 2, true, layer5)
        val layer3 = JsonArray(layer4, "word1", "word2")
        val layer2 = JsonObject("not'_'this" to "true", ".this." to layer3)
        val root = JsonArray("many", 88, "are", false, layer2)

        val result = root.navigateToTarget(".[4].\\.this\\..[0].[3].  .array.[2]")

        assertEquals("direct access to field", layer8, result)
    }

    @Test
    fun `test accessing field in objects in array`() {
        val item1 = JsonObject("foo" to "bar")
        val item2 = JsonObject("foo" to "baz")
        val root = JsonArray(item1, item2)

        val result = root.navigateToTarget(".foo")

        assertEquals("array of foo", listOf("bar", "baz"), result)
    }
}