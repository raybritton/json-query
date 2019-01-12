package com.raybritton.jsonquery.unit.filtering

import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.tools.filterToProjection
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectionTests {
    @Test
    fun `test single field select projection on object`() {
        val json = JsonObject("foo" to true, "bar" to 12, "baz" to "word")
        val query = Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(SelectProjection.SingleField("baz"), null, null, null))

        val result = json.filterToProjection(query)

        assertEquals(JsonObject("baz" to "word"), result)
    }

    @Test
    fun `test multiple field select projection on object`() {
        val json = JsonObject("foo" to true, "bar" to 12, "baz" to "word")
        val query = Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(SelectProjection.MultipleFields(listOf("baz", "bar")), null, null, null))

        val result = json.filterToProjection(query)

        assertEquals(JsonObject("bar" to 12, "baz" to "word"), result)
    }

    @Test
    fun `test single field select projection on nested object`() {
        val json = JsonObject("inner" to JsonObject("foo" to 1, "bar" to 2))
        val query = Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(SelectProjection.SingleField("inner.foo"), null, null, null))

        val result = json.filterToProjection(query)

        assertEquals(JsonObject("foo" to 1), result)
    }

    @Test
    fun `test multiple field select projection on nested object`() {
        val json = JsonObject("inner" to JsonObject("foo" to 1, "bar" to 2))
        val query = Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(SelectProjection.MultipleFields(listOf("inner.foo", "inner.bar")), null, null, null))

        val result = json.filterToProjection(query)

        assertEquals(JsonObject("foo" to 1, "bar" to 2), result)
    }

    @Test
    fun `test fields at different levels select projection on nested object`() {
        val json = JsonObject("inner" to JsonObject("foo" to 1, "inner2" to JsonObject("bar" to 2)))
        val query = Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(SelectProjection.MultipleFields(listOf("inner.foo", "inner.inner2.bar")), null, null, null))

        val result = json.filterToProjection(query)

        assertEquals(JsonObject("foo" to 1, "bar" to 2), result)
    }

    @Test
    fun `test multiple field select projection on nested object in array`() {
        val json = JsonArray(JsonObject("inner" to JsonObject("foo" to 1, "bar" to 2)))
        val query = Query("", Query.Method.SELECT, Target.TargetField("."), Query.Flags(), null, select = SelectQuery(SelectProjection.MultipleFields(listOf("inner.foo", "inner.bar")), null, null, null))

        val result = json.filterToProjection(query)

        assertEquals(JsonArray(JsonObject("foo" to 1, "bar" to 2)), result)
    }
}