package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectTests {
    @Test
    fun `test selecting everything`() {
        val json = """{"foo": true, "bar": false, "baz": 123}"""

        val result = JsonQuery(json).query("SELECT '.'")

        assertEquals("""{bar: false, foo: true, baz: 123.0}""", result)
    }

    @Test
    fun `test selecting everything as json`() {
        val json = """{"foo": true, "bar": false, "baz": 123}"""

        val result = JsonQuery(json).query("SELECT '.' AS JSON")

        assertEquals("""{"bar":false,"foo":true,"baz":123.0}""", result)
    }

    @Test
    fun `test selecting with where`() {
        val json = """{"foo": true, "bar": false, "baz": 123}"""

        val result = JsonQuery(json).query("SELECT '.' WHERE 'foo' == TRUE")

        assertEquals("""{bar: false, foo: true, baz: 123.0}""", result)
    }

    @Test
    fun `test selecting field with where`() {
        val json = """{"foo": true, "bar": false, "baz": 123}"""

        val result = JsonQuery(json).query("SELECT 'bar' FROM '.' WHERE 'foo' == TRUE")

        assertEquals("""bar: false""", result)
    }

    @Test
    fun `test selecting field with rename`() {
        val json = """{"foo": true, "bar": false, "baz": 123}"""

        val result = JsonQuery(json).query("SELECT 'bar' AS 'new' FROM '.'")

        assertEquals("""new: false""", result)
    }
}