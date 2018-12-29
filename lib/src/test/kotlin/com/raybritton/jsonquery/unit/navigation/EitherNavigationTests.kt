package com.raybritton.jsonquery.unit.navigation

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.tools.navigateToProjection
import com.raybritton.jsonquery.tools.navigateToTarget
import org.junit.Assert.assertEquals
import org.junit.Test

class EitherNavigationTests {

    @Test
    fun `test navigating to field in root object`() {
        val obj = JsonObject("num" to 1, "str" to "test", "bool" to true)

        val num = obj.navigateToProjection(".num")
        val str = obj.navigateToProjection(".str")
        val bool = obj.navigateToProjection(".bool")

        assertEquals("number", 1, num)
        assertEquals("string", "test", str)
        assertEquals("boolean", true, bool)
    }

    @Test
    fun `test navigating to element in root array`() {
        val arr = JsonArray(1, 2, 3)

        val result = arr.navigateToProjection(".[1]")

        assertEquals("second element", 2, result)
    }

    @Test
    fun `test navigating to element in array in object in array at root`() {
        val innerArray = JsonArray("the word true", true, 1)
        val innerObject = JsonObject("the_array" to innerArray)
        val root = JsonArray(innerObject)

        val result = root.navigateToProjection(".[0].the_array.[1]")

        assertEquals("second element in inner array", true, result)
    }

    @Test
    fun `test accessing field in root object containing square brackets`() {
        val root = JsonObject("[0]" to "target")

        val result = root.navigateToProjection(".[0]")

        assertEquals("field", "target", result)
    }

    @Test
    fun `test accessing field in root object containing escaped periods`() {
        val root = JsonObject("foo." to 108)

        val result = root.navigateToProjection(".foo\\.")

        assertEquals("escaped field", 108, result)
    }

    @Test
    fun `test accessing field in root object containing escaped quotes`() {
        val root = JsonObject("foo\"" to "word")

        val result = root.navigateToProjection(".foo\"")

        assertEquals("escaped field", "word", result)
    }

    @Test
    fun `test accessing field in root object containing whitespace`() {
        val root = JsonObject("  " to "bar")

        val result = root.navigateToProjection("  ")

        assertEquals("whitespace field", "bar", result)
    }

    @Test
    fun `test accessing field in object in root object containing whitespace`() {
        val inner = JsonObject("foo" to "bar")
        val root = JsonObject("  " to inner)

        val result = root.navigateToProjection(".  .foo")

        assertEquals("whitespace field in object", "bar", result)
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

        val result1 = root.navigateToProjection(".[4].\\.this\\..[0].[3].  .array.[2].field")
        val result2 = root.navigateToProjection(".[4].\\.this\\..[0].[3].  .array.field")

        assertEquals("direct access to field", 1, result1)
        assertEquals("indirect access to field", 1, result2)
    }

    @Test
    fun `test navigating to object in root array`() {
        val item1 = JsonObject("foo" to 1)
        val item2 = JsonObject("bar" to 2)
        val array = JsonArray(item1, item2)

        val result = array.navigateToProjection(".foo")

        assertEquals("Value from obj in arr", 1, result)
    }

    @Test
    fun `test navigating to value and then to value via object`() {
        val json = JsonArray(JsonObject("foo" to JsonObject("bar" to 12)))

        val result1 = json.navigateToProjection("foo.bar")
        val result2 = json.navigateToTarget(".foo").navigateToProjection("bar")

        assertEquals("directly", 12, result1)
        assertEquals("via foo", 12, result2)
    }
}