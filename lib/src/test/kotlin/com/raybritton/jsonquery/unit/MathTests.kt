package com.raybritton.jsonquery.unit

import com.raybritton.jsonquery.models.ElementFieldProjection
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.tools.math
import org.junit.Assert.assertEquals
import org.junit.Test

class MathTests {
    @Test
    fun testAllPositiveMin() {
        val array = JsonArray(10, 20, 23, 25)

        val min = array.math(Keyword.MIN, ElementFieldProjection.Element)

        assertEquals("min", 10.0, min, 0.0)
    }

    @Test
    fun testAllNegativeMin() {
        val array = JsonArray(-10, -5, -1)

        val min = array.math(Keyword.MIN, ElementFieldProjection.Field(""))

        assertEquals("min", -10.0, min, 0.0)
    }

    @Test
    fun testMixedMin() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val min = array.math(Keyword.MIN, ElementFieldProjection.Field(""))

        assertEquals("min", -10.56, min, 0.0)
    }

    @Test
    fun testAllPositiveMax() {
        val array = JsonArray(10, 20, 23, 25)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Element)

        assertEquals("max", 25.0, max, 0.0)
    }

    @Test
    fun testAllNegativeMax() {
        val array = JsonArray(-10, -5, -1)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Field(""))

        assertEquals("max", -1.0, max, 0.0)
    }

    @Test
    fun testMixedMax() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val max = array.math(Keyword.MAX, ElementFieldProjection.Field(""))

        assertEquals("max", 1.0, max, 0.0)
    }

    @Test
    fun testAllPositiveSum() {
        val array = JsonArray(10, 20, 23, 25)

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Element)

        assertEquals("max", 78.0, sum, 0.0)
    }

    @Test
    fun testAllNegativeSum() {
        val array = JsonArray(-10, -5, -1)

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Field(""))

        assertEquals("max", -16.0, sum, 0.0)
    }

    @Test
    fun testMixedSum() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val sum = array.math(Keyword.SUM, ElementFieldProjection.Field(""))

        assertEquals("max", -14.56, sum, 0.0)
    }

    @Test
    fun testEmptyArrayCount() {
        val array = JsonArray()

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Element)

        assertEquals("count", 0.0, count, 0.0)
    }

    @Test
    fun testArrayCount() {
        val array = JsonArray(-10.56, -5, 0, 1)

        val count = array.math(Keyword.COUNT, ElementFieldProjection.Field(""))

        assertEquals("count", 4.0, count, 0.0)
    }
}