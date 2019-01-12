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

    private fun makeQuery(oldFields: List<String>, newFields: List<String>): Query {
        val projection = when {
            oldFields.size == 1 -> {
                SelectProjection.SingleField(oldFields[0], newFields[0])
            }
            else -> throw IllegalArgumentException("Can't make projection")
        }
        return Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(projection, null, null, null))
    }
}