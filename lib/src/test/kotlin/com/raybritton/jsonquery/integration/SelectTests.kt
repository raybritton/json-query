package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import com.raybritton.jsonquery.SyntaxException
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class SelectTests {
    @Rule
    @JvmField
    var exceptionRule: ExpectedException = ExpectedException.none()

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

    @Test
    fun `test selecting keys`() {
        val json = """{'a': 1, 'b':true, 'c':'word'}"""

        val result = JsonQuery(json).query("""SELECT KEYS FROM '.'""")

        assertEquals("""{a, b, c}""", result)
    }

    @Test
    fun `test selecting values`() {
        val json = """{'a': 1, 'b':true, 'c':'word'}"""

        val result = JsonQuery(json).query("""SELECT VALUES FROM '.'""")

        assertEquals("{1.0, true, \"word\"}", result)
    }

    @Test
    fun `test selecting keys as json`() {
        exceptionRule.expect(SyntaxException::class.java)
        exceptionRule.expectMessage("AS JSON can not be used with KEYS")

        val json = """{'a': 1, 'b':true, 'c':'word'}"""

        val result = JsonQuery(json).query("""SELECT KEYS FROM '.' AS JSON""")
    }

    @Test
    fun `test selecting values as json`() {
        exceptionRule.expect(SyntaxException::class.java)
        exceptionRule.expectMessage("AS JSON can not be used with VALUES")

        val json = """{'a': 1, 'b':true, 'c':'word'}"""

        val result = JsonQuery(json).query("""SELECT VALUES FROM '.' AS JSON""")
    }
}