package com.raybritton.jsonquery.unit.printers

import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.printers.SelectPrinter
import org.junit.Assert.assertEquals
import org.junit.Test

class SelectPrinterTests {
    @Test
    fun `test simple object`() {
        val obj = JsonObject("a" to 1, "b" to true, "c" to "word", "d" to null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(), null)

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple object", """{a: 1, b: true, c: "word", d: null}""", result)
    }

    @Test
    fun `test simple object as json`() {
        val obj = JsonObject("a" to 1, "b" to true, "c" to "word", "d" to null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true), null)

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple object as json", """{"a":1,"b":true,"c":"word","d":null}""", result)
    }

    @Test
    fun `test simple object as json pretty`() {
        val obj = JsonObject("a" to 1, "b" to true, "c" to "word", "d" to null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true, isPrettyPrinted = true), null)

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple object as json pretty", """{
                                                                  |  "a": 1,
                                                                  |  "b": true,
                                                                  |  "c": "word",
                                                                  |  "d": null
                                                                  |}""".trimMargin(), result)
    }

    @Test
    fun `test simple object keys only`() {
        val obj = JsonObject("a" to 1, "b" to true, "c" to "word", "d" to null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintKeys = true), null)

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple object with keys only", """{a, b, c, d}""", result)
    }

    @Test
    fun `test simple object values only`() {
        val obj = JsonObject("a" to 1, "b" to true, "c" to "word", "d" to null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintValues = true), null)

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple object with values only", """{1, true, "word", null}""", result)
    }

    @Test
    fun `test simple array`() {
        val obj = JsonArray(1, 2, "word", false)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple array", """[1, 2, "word", false]""", result)
    }

    @Test
    fun `test simple array as json`() {
        val obj = JsonArray(1, 2, "word", false)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple array as json", """[1,2,"word",false]""", result)
    }

    @Test
    fun `test simple array as json pretty`() {
        val obj = JsonArray(1, 2, "word", false)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true, isPrettyPrinted = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple array as json", """[
                                                                    |  1,
                                                                    |  2,
                                                                    |  "word",
                                                                    |  false
                                                                    |]""".trimMargin(), result)
    }

    @Test
    fun `test simple array keys only`() {
        val obj = JsonArray(1, 2, "word", false)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintKeys = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple array keys only", """[1, 2, "word", false]""", result)
    }

    @Test
    fun `test simple array values only`() {
        val obj = JsonArray(1, 2, "word", false)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintValues = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("simple array values only", """[1, 2, "word", false]""", result)
    }

    @Test
    fun `test nested objects`() {
        val obj = JsonObject("number" to 100, "inner" to JsonObject("foo" to true, "bar" to false))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{number: 100, inner: {bar: false, foo: true}}""", result)
    }

    @Test
    fun `test nested objects as json`() {
        val obj = JsonObject("number" to 100, "inner" to JsonObject("foo" to true, "bar" to false))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{"number":100,"inner":{"bar":false,"foo":true}}""", result)
    }

    @Test
    fun `test nested objects as json pretty`() {
        val obj = JsonObject("number" to 100, "inner" to JsonObject("foo" to true, "bar" to false))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true, isPrettyPrinted = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{
                                                            |  "number": 100,
                                                            |  "inner": {
                                                            |    "bar": false,
                                                            |    "foo": true
                                                            |  }
                                                            |}""".trimMargin(), result)
    }

    @Test
    fun `test nested objects keys only`() {
        val obj = JsonObject("number" to 100, "inner" to JsonObject("foo" to true, "bar" to false))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintKeys = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{number, inner}""", result)
    }

    @Test
    fun `test nested objects values only`() {
        val obj = JsonObject("number" to 100, "inner" to JsonObject("foo" to true, "bar" to false))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintValues = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{100, {false, true}}""", result)
    }

    @Test
    fun `test nested arrays`() {
        val obj = JsonArray(JsonArray("foo", "bar"), JsonArray(1, 2, 3), true, null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested arrays", """[["foo", "bar"], [1, 2, 3], true, null]""", result)
    }

    @Test
    fun `test nested arrays as json`() {
        val obj = JsonArray(JsonArray("foo", "bar"), JsonArray(1, 2, 3), true, null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested arrays", """[["foo","bar"],[1,2,3],true,null]""", result)
    }

    @Test
    fun `test nested arrays as json pretty`() {
        val obj = JsonArray(JsonArray("foo", "bar"), JsonArray(1, 2, 3), true, null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true, isPrettyPrinted = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested arrays", """[
                                                |  [
                                                |    "foo",
                                                |    "bar"
                                                |  ],
                                                |  [
                                                |    1,
                                                |    2,
                                                |    3
                                                |  ],
                                                |  true,
                                                |  null
                                                |]""".trimMargin(), result)
    }

    @Test
    fun `test nested arrays keys only`() {
        val obj = JsonArray(JsonArray("foo", "bar"), JsonArray(1, 2, 3), true, null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintKeys = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested arrays", """[["foo", "bar"], [1, 2, 3], true, null]""", result)
    }

    @Test
    fun `test nested arrays values only`() {
        val obj = JsonArray(JsonArray("foo", "bar"), JsonArray(1, 2, 3), true, null)
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintValues = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested arrays", """[["foo", "bar"], [1, 2, 3], true, null]""", result)
    }

    @Test
    fun `test objects and arrays`() {
        val obj = JsonObject("foo" to "bar", "foobar" to "baz", "codes" to JsonArray(234, 3424, 1, 23, 432, 1, 12, 3), "things" to JsonArray(JsonObject("a" to 12, "b" to "foo"), JsonObject("a" to 10, "b" to "bar"), JsonObject("a" to 13, "b" to "baz")))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{things: [{a: 12, b: "foo"}, {a: 10, b: "bar"}, {a: 13, b: "baz"}], codes: [234, 3424, 1, 23, 432, 1, 12, 3], foobar: "baz", foo: "bar"}""", result)
    }

    @Test
    fun `test objects and arrays as json`() {
        val obj = JsonObject("foo" to "bar", "foobar" to "baz", "codes" to JsonArray(234, 3424, 1, 23, 432, 1, 12, 3), "things" to JsonArray(JsonObject("a" to 12, "b" to "foo"), JsonObject("a" to 10, "b" to "bar"), JsonObject("a" to 13, "b" to "baz")))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{"things":[{"a":12,"b":"foo"},{"a":10,"b":"bar"},{"a":13,"b":"baz"}],"codes":[234,3424,1,23,432,1,12,3],"foobar":"baz","foo":"bar"}""", result)
    }

    @Test
    fun `test objects and arrays as json pretty`() {
        val obj = JsonObject("foo" to "bar", "foobar" to "baz", "codes" to JsonArray(234, 3424, 1, 23, 432, 1, 12, 3), "things" to JsonArray(JsonObject("a" to 12, "b" to "foo"), JsonObject("a" to 10, "b" to "bar"), JsonObject("a" to 13, "b" to "baz")))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isAsJson = true, isPrettyPrinted = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{
                                                            |  "things": [
                                                            |    {
                                                            |      "a": 12,
                                                            |      "b": "foo"
                                                            |    },
                                                            |    {
                                                            |      "a": 10,
                                                            |      "b": "bar"
                                                            |    },
                                                            |    {
                                                            |      "a": 13,
                                                            |      "b": "baz"
                                                            |    }
                                                            |  ],
                                                            |  "codes": [
                                                            |    234,
                                                            |    3424,
                                                            |    1,
                                                            |    23,
                                                            |    432,
                                                            |    1,
                                                            |    12,
                                                            |    3
                                                            |  ],
                                                            |  "foobar": "baz",
                                                            |  "foo": "bar"
                                                            |}""".trimMargin(), result)
    }

    @Test
    fun `test objects and arrays keys only`() {
        val obj = JsonObject("foo" to "bar", "foobar" to "baz", "codes" to JsonArray(234, 3424, 1, 23, 432, 1, 12, 3), "things" to JsonArray(JsonObject("a" to 12, "b" to "foo"), JsonObject("a" to 10, "b" to "bar"), JsonObject("a" to 13, "b" to "baz")))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintKeys = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{things, codes, foobar, foo}""", result)
    }

    @Test
    fun `test objects and arrays values only`() {
        val obj = JsonObject("foo" to "bar", "foobar" to "baz", "codes" to JsonArray(234, 3424, 1, 23, 432, 1, 12, 3), "things" to JsonArray(JsonObject("a" to 12, "b" to "foo"), JsonObject("a" to 10, "b" to "bar"), JsonObject("a" to 13, "b" to "baz")))
        val query = Query("", Query.Method.SELECT, Target.TargetField(""), Query.Flags(isOnlyPrintValues = true), null, select = SelectQuery(SelectProjection.All, null, null, null))

        val result = SelectPrinter().print(obj, query)

        assertEquals("nested objects", """{[{12, "foo"}, {10, "bar"}, {13, "baz"}], [234, 3424, 1, 23, 432, 1, 12, 3], "baz", "bar"}""", result)
    }
}