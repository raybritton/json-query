package com.raybritton.jsonquery.unit.printers

import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.printers.SearchPrinter
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchPrinterTests {
    @Test
    fun `test simple object (== string)`() {
        val obj = JsonObject("a" to 78, "b" to "word", "c" to true, "d" to null)
        val query = Query("", Query.Method.SEARCH, Target.TargetField("."), Query.Flags(), null, search = SearchQuery(SearchQuery.TargetRange.ANY, Operator.Equal, Value.ValueString("word")))

        val result = SearchPrinter.print(obj, query)

        assertEquals("single result", """.b""", result)
    }

    @Test
    fun `test simple object with values (== string)`() {
        val obj = JsonObject("a" to 78, "b" to "word", "c" to true, "d" to null)
        val query = Query("", Query.Method.SEARCH, Target.TargetField("."), Query.Flags(isWithValues = true), null, search = SearchQuery(SearchQuery.TargetRange.ANY, Operator.Equal, Value.ValueString("word")))

        val result = SearchPrinter.print(obj, query)

        assertEquals("single result", """.b: word""", result)
    }

    @Test
    fun `test simple nested object (== string)`() {
        val obj = JsonObject("foo" to "a", "inner" to JsonObject("bar" to "b", "nested" to JsonObject("baz" to "answer")))
        val query = Query("", Query.Method.SEARCH, Target.TargetField("."), Query.Flags(), null, search = SearchQuery(SearchQuery.TargetRange.ANY, Operator.Equal, Value.ValueString("answer")))

        val result = SearchPrinter.print(obj, query)

        assertEquals("single result", """.inner.nested.baz""", result)
    }

    @Test
    fun `test simple nested object with values (== string)`() {
        val obj = JsonObject("foo" to "a", "inner" to JsonObject("bar" to "b", "nested" to JsonObject("baz" to "answer")))
        val query = Query("", Query.Method.SEARCH, Target.TargetField("."), Query.Flags(isWithValues = true), null, search = SearchQuery(SearchQuery.TargetRange.ANY, Operator.Equal, Value.ValueString("answer")))

        val result = SearchPrinter.print(obj, query)

        assertEquals("single result", """.inner.nested.baz: answer""", result)
    }

    @Test
    fun `test multiple matches simple object (== string)`() {
        val obj = JsonObject("foo" to "a", "bar" to "b", "baz" to "a")
        val query = Query("", Query.Method.SEARCH, Target.TargetField("."), Query.Flags(), null, search = SearchQuery(SearchQuery.TargetRange.ANY, Operator.Equal, Value.ValueString("a")))

        val result = SearchPrinter.print(obj, query)

        assertEquals("single result", """.baz
                                                 |.foo
        """.trimMargin(), result)
    }
}