package com.raybritton.jsonquery.unit.printers

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.printers.DescribePrinter
import org.junit.Assert.assertEquals
import org.junit.Test

class DescribePrinterTests {
    @Test
    fun `test simple object`() {
        val obj = JsonObject("a" to 78, "b" to "word", "c" to true, "d" to null)
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(), null)

        val result = DescribePrinter.print(obj, query)

        assertEquals("simple object", """OBJECT(NUMBER, STRING, BOOLEAN, NULL)""", result)
    }

    @Test
    fun `test simple array`() {
        val obj = JsonArray(-90, 10, "foo", false, null)
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(), null)

        val result = DescribePrinter.print(obj, query)

        assertEquals("simple array", """ARRAY(NUMBER[2], STRING, BOOLEAN, NULL)""", result)
    }

    @Test
    fun `test array of objects`() {
        val obj = JsonArray(JsonObject("foo" to "a"), JsonObject("bar" to "b"), JsonObject("baz" to 1))
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(), null)

        val result = DescribePrinter.print(obj, query)

        assertEquals("objects in array", """ARRAY(OBJECT(STRING)[2], OBJECT(NUMBER))""", result)
    }

    @Test
    fun `test array of objects with keys`() {
        val obj = JsonArray(JsonObject("foo" to "a"), JsonObject("bar" to "b"), JsonObject("baz" to 1))
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(isWithKeys = true), null)

        val result = DescribePrinter.print(obj, query)

        assertEquals("objects in array", """ARRAY(OBJECT(foo: STRING), OBJECT(bar: STRING), OBJECT(baz: NUMBER))""", result)
    }

    @Test
    fun `test array of objects same keys with keys`() {
        val obj = JsonArray(JsonObject("foo" to "a"), JsonObject("foo" to "b"), JsonObject("foo" to 1))
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(isWithKeys = true), null)

        val result = DescribePrinter.print(obj, query)

        assertEquals("objects in array", """ARRAY(OBJECT(foo: STRING)[2], OBJECT(foo: NUMBER))""", result)
    }
}