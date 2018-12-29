package com.raybritton.jsonquery.unit.navigation

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.tools.navigateToProjection
import com.raybritton.jsonquery.tools.navigateToTarget
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class InvalidNavigationTests {
    @Rule
    @JvmField
    var exceptionRule: ExpectedException = ExpectedException.none()

    @Test
    fun `test no matching field in object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Found nothing for '.bar'")

        val root = JsonObject("foo" to 1)

        root.navigateToTarget(".bar")
    }

    @Test
    fun `test index too high in array`() {
        exceptionRule.expect(IllegalStateException::class.java)
        exceptionRule.expectMessage("Index too high for '.[10]'")

        val root = JsonArray(1, 2, 3, 4, 5)

        root.navigateToTarget(".[10]")
    }

    @Test
    fun `test accessing field in array with no objects`() {
        exceptionRule.expect(IllegalStateException::class.java)
        exceptionRule.expectMessage("No objects found for '.foo'")

        val root = JsonArray(1, 2, 3, 4, 5)

        root.navigateToTarget(".foo")
    }

    @Test
    fun `test negative index in array`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("No objects found for '.[-1]'")

        val root = JsonArray("foo")

        root.navigateToTarget(".[-1]")
    }

    @Test
    fun `test accessing null field in root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a null value")

        val root = JsonObject("foo" to null)

        root.navigateToTarget("foo")
    }

    @Test
    fun `test accessing null field in root array`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a null value")

        val root = JsonArray("foo", null)

        root.navigateToTarget(".[1]")
    }

    @Test
    fun `test navigating to the root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Path '.' resulted in an object or array")

        val obj = JsonObject("foo" to 1)

        obj.navigateToProjection(".")
    }

    @Test
    fun `test navigating to the root array`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Path '.' resulted in an object or array")

        val arr = JsonArray(1, 2)

        arr.navigateToProjection(".")
    }

    @Test
    fun `test navigating to object in root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Path '.foo' resulted in an object or array")

        val inner = JsonObject("bar" to "test")
        val obj = JsonObject("foo" to inner)

        obj.navigateToProjection(".foo")
    }

    @Test
    fun `test navigating to object in in object in root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Path '.foo.bar' resulted in an object or array")

        val deep = JsonObject("baz" to "test")
        val inner = JsonObject("bar" to deep)
        val obj = JsonObject("foo" to inner)

        obj.navigateToProjection(".foo.bar")
    }

    @Test
    fun `test navigating to object in root array`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val item1 = JsonObject("foo" to 1)
        val item2 = JsonObject("bar" to 2)
        val array = JsonArray(item1, item2)

        array.navigateToTarget(".foo")
    }

    @Test
    fun `test navigating to a number in root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val obj = JsonObject("num" to 1, "str" to "test", "bool" to true)

        obj.navigateToTarget(".num")
    }

    @Test
    fun `test navigating to a string in root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val obj = JsonObject("num" to 1, "str" to "test", "bool" to true)

        obj.navigateToTarget(".str")
    }

    @Test
    fun `test navigating to a boolean in root object`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val obj = JsonObject("num" to 1, "str" to "test", "bool" to true)

        obj.navigateToTarget(".bool")
    }

    @Test
    fun `test navigating to element in root array`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val arr = JsonArray(1, 2, 3)

        arr.navigateToTarget(".[1]")
    }

    @Test
    fun `test navigating to element in array in object in array at root`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val innerArray = JsonArray("the word true", true, 1)
        val innerObject = JsonObject("the_array" to innerArray)
        val root = JsonArray(innerObject)

        root.navigateToTarget(".[0].the_array.[1]")
    }

    @Test
    fun `test accessing field in root object containing square brackets`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val root = JsonObject("[0]" to "target")

        root.navigateToTarget(".[0]")
    }

    @Test
    fun `test accessing field in root object containing escaped periods`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val root = JsonObject("foo." to 108)

        root.navigateToTarget(".foo\\.")
    }

    @Test
    fun `test accessing field in root object containing escaped quotes`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val root = JsonObject("foo\"" to "word")

        root.navigateToTarget(".foo\"")
    }

    @Test
    fun `test accessing field in root object containing whitespace`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val root = JsonObject("  " to "bar")

        root.navigateToTarget("  ")
    }

    @Test
    fun `test accessing field in object in root object containing whitespace`() {
        exceptionRule.expect(RuntimeException::class.java)
        exceptionRule.expectMessage("Target path resulted in a value")

        val inner = JsonObject("foo" to "bar")
        val root = JsonObject("  " to inner)

        root.navigateToTarget(".  .foo")
    }
}