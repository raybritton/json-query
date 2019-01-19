package com.raybritton.jsonquery.unit

import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.tools.rewrite
import org.junit.Assert.assertEquals
import org.junit.Test

class RewritingTests {
    @Test
    fun `test rewriting field in same object`() {
        val json = JsonObject("foo" to 12)
        val query = makeQuery(listOf("foo"), listOf("bar"))

        json.rewrite(query)
        assertEquals(JsonObject("bar" to 12), json)
    }

    @Test
    fun `test rewriting field in same nested object`() {
        val json = JsonObject("inner" to JsonObject("foo" to 12))
        val query = makeQuery(listOf("inner.foo"), listOf("inner.bar"))

        json.rewrite(query)
        assertEquals(JsonObject("inner" to JsonObject("bar" to 12)), json)
    }

    @Test
    fun `test rewriting field to different object`() {
        val json = JsonObject("inner1" to JsonObject("foo" to 12))
        val query = makeQuery(listOf("inner1.foo"), listOf("inner2.foo"))

        json.rewrite(query)
        assertEquals(JsonObject("inner2" to JsonObject("foo" to 12)), json)
    }

    @Test
    fun `test rewriting field to parent object`() {
        val json = JsonObject("inner" to JsonObject("foo" to 12))
        val query = makeQuery(listOf("inner.foo"), listOf("bar"))

        json.rewrite(query)
        assertEquals(JsonObject("bar" to 12), json)
    }

    @Test
    fun `test rewriting field in same object in array`() {
        val json = JsonObject("foo" to JsonArray(JsonObject("bar" to "word")))
        val query = makeQuery(listOf("foo.bar"), listOf("foo.baz"))

        json.rewrite(query)
        assertEquals(JsonObject("foo" to JsonArray(JsonObject("baz" to "word"))), json)
    }

    @Test
    fun `test rewriting field to different object in array`() {
        val json = JsonObject("foo" to JsonArray(JsonObject("bar" to "word")))
        val query = makeQuery(listOf("foo.bar"), listOf("baz"))

        json.rewrite(query)
        assertEquals(JsonObject("baz" to JsonArray("word")), json)
    }

    @Test
    fun `test rewriting two fields in same object`() {
        val json = JsonObject("foo" to 12, "bar" to true)
        val query = makeQuery(listOf("foo", "bar"), listOf("fooer", "barer"))

        json.rewrite(query)
        assertEquals(JsonObject("fooer" to 12, "barer" to true), json)
    }

    @Test
    fun `test rewriting fields in multiple object`() {
        val json = JsonObject("inner" to JsonObject("bar" to "word"), "foo" to 80)
        val query = makeQuery(listOf("foo", "inner.bar"), listOf("bar", "inner.baz"))

        json.rewrite(query)
        assertEquals(JsonObject("bar" to 80, "inner" to JsonObject("baz" to "word")), json)
    }

    @Test
    fun `test object creation`() {
        val json = JsonObject("foo" to 12)
        val query = makeQuery(listOf("foo"), listOf("inner.foo"))

        json.rewrite(query)
        assertEquals(JsonObject("inner" to JsonObject("foo" to 12)), json)
    }

    @Test
    fun `test nested object creation`() {
        val json = JsonObject("foo" to 12)
        val query = makeQuery(listOf("foo"), listOf("inner.more.foo"))

        json.rewrite(query)
        assertEquals(JsonObject("inner" to JsonObject("more" to JsonObject("foo" to 12))), json)
    }

    @Test
    fun `test object creation in root array`() {
        val json = JsonArray(JsonObject("foo" to 12))
        val query = makeQuery(listOf("foo"), listOf("inner.foo"))

        json.rewrite(query)
        assertEquals(JsonArray(JsonObject("inner" to JsonObject("foo" to 12))), json)
    }

    @Test
    fun `test object creation in array`() {
        val json = JsonObject("outer" to JsonArray(JsonObject("foo" to 12)))
        val query = makeQuery(listOf("outer.foo"), listOf("outer.inner.foo"))

        json.rewrite(query)
        assertEquals(JsonObject("outer" to JsonArray(JsonObject("inner" to JsonObject("foo" to 12)))), json)
    }

    private fun makeQuery(oldFields: List<String>, newFields: List<String>): Query {
        val projection = when {
            oldFields.size == 1 -> {
                SelectProjection.SingleField(oldFields[0], newFields[0])
            }
            oldFields.size > 1 -> {
                val fields = oldFields.mapIndexed { idx, field -> field to newFields[idx] }
                SelectProjection.MultipleFields(fields)
            }
            else -> throw IllegalArgumentException("Can't make projection")
        }
        return Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(projection, null, null, null))
    }
}