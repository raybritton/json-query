package com.raybritton.jsonquery.unit

import com.raybritton.jsonquery.models.ElementFieldProjection
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.tools.math
import org.junit.Assert.assertEquals
import org.junit.Test

class MathTests {
    @Test
    fun `test minimum for all positive array`() {
        val array = JsonArray(10, 20, 23, 25)

        val min = array.math(Keyword.MIN, ElementFieldProjection.Element, false) as Double

        assertEquals("min", 10.0, min, 0.0)
    }

    @Test
    fun `test minimum for all negative array`() {
        val array = JsonArray(-10, -5, -1)

        val min = array.math(Keyword.MIN, ElementFieldProjection.Field(""), false) as Double

        assertEquals("min", -10.0, min, 0.0)
    }

    @Test
    fun `test minimum for mixed sign array`() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val min = array.math(Keyword.MIN, ElementFieldProjection.Field(""), false) as Double

        assertEquals("min", -10.56, min, 0.0)
    }

    @Test
    fun `test minimum for mixed array`() {
        val array = JsonArray(-10.56, -5, 0, JsonObject("not" to "this"))

        val min = array.math(Keyword.MIN, ElementFieldProjection.Field(""), false) as Double

        assertEquals("min", -10.56, min, 0.0)
    }

    @Test
    fun `test maximum for all positive array`() {
        val array = JsonArray(10, 20, 23, 25)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Element, false) as Double

        assertEquals("max", 25.0, max, 0.0)
    }

    @Test
    fun `test maximum for all negative array`() {
        val array = JsonArray(-10, -5, -1)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Field(""), false) as Double

        assertEquals("max", -1.0, max, 0.0)
    }

    @Test
    fun `test maximum for mixed sign array`() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Field(""), false) as Double

        assertEquals("max", 1.0, max, 0.0)
    }

    @Test
    fun `test maximum for mixed array`() {
        val array = JsonArray(-10.56, -5, 0, true)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Field(""), false) as Double

        assertEquals("max", 0.0, max, 0.0)
    }

    @Test
    fun `test sum for all positive array`() {
        val array = JsonArray(10, 20, 23, 25)

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Element, false) as Double

        assertEquals("max", 78.0, sum, 0.0)
    }

    @Test
    fun `test sum for all negative array`() {
        val array = JsonArray(-10, -5, -1)

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Field(""), false) as Double

        assertEquals("max", -16.0, sum, 0.0)
    }

    @Test
    fun `test sum for mixed sign array`() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Field(""), false) as Double

        assertEquals("max", -14.56, sum, 0.0)
    }

    @Test
    fun `test sum for mixed array`() {
        val array = JsonArray(-10.56, -5, 0, "hello")

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Field(""), false) as Double

        assertEquals("max", -15.56, sum, 0.0)
    }

    @Test
    fun `test count for empty array`() {
        val array = JsonArray()

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Element, false) as Double

        assertEquals("count", 0.0, count, 0.0)
    }

    @Test
    fun `test count for array of mixed sign numbers`() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 4.0, count, 0.0)
    }

    @Test
    fun `test count for array of words`() {
        val array = JsonArray("test", "word")

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 2.0, count, 0.0)
    }

    @Test
    fun `test count for array of booleans`() {
        val array = JsonArray(true, false, true)

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 3.0, count, 0.0)
    }

    @Test
    fun `test count for array of nulls`() {
        val array = JsonArray(null, null, null, null)

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 4.0, count, 0.0)
    }

    @Test
    fun `test count for array of objects`() {
        val array = JsonArray(JsonObject(), JsonObject("foo" to "bar"))

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 2.0, count, 0.0)
    }

    @Test
    fun `test count for array of arrays`() {
        val array = JsonArray(JsonArray(), JsonArray(1, 2, 3))

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 2.0, count, 0.0)
    }

    @Test
    fun `test count for array of mixed`() {
        val array = JsonArray("test", true, null, JsonObject(), JsonArray(6, 7, 8), 1)

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""), false) as Double

        assertEquals("count", 6.0, count, 0.0)
    }
}