package com.raybritton.jsonquery.unit.filtering

import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.models.Where
import com.raybritton.jsonquery.models.WhereProjection
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.tools.where
import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleObjectTests {

    @Test
    fun `test equal object with no matches`() {
        val json = JsonObject("foo" to 12, "bar" to "baz")
        val where = Where(WhereProjection.Field("x"), Operator.Equal, Value.ValueString(""))

        val result = json.where(where, false, null)

        assertEquals(JsonObject(), result)
    }

    @Test
    fun `test equal object with match`() {
        val json = JsonObject("foo" to "bar")
        val where = Where(WhereProjection.Field("foo"), Operator.Equal, Value.ValueString("bar"))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }

    @Test
    fun `test equal object field contains match`() {
        val json = JsonObject("foo" to "bar")
        val where = Where(WhereProjection.Field("foo"), Operator.Contains, Value.ValueString("ba"))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }

    @Test
    fun `test equal object field not contains match`() {
        val json = JsonObject("foo" to "bar")
        val where = Where(WhereProjection.Field("foo"), Operator.Contains, Value.ValueString("baz"))

        val result = json.where(where, false, null)

        assertEquals(JsonObject(), result)
    }

    @Test
    fun `test equal object field less than`() {
        val json = JsonObject("foo" to "bar")
        val where = Where(WhereProjection.Field("foo"), Operator.LessThan, Value.ValueString("z"))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }

    @Test
    fun `test equal object field greater than`() {
        val json = JsonObject("foo" to "bar")
        val where = Where(WhereProjection.Field("foo"), Operator.GreaterThan, Value.ValueString("a"))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }

    @Test
    fun `test field from nested object not matching`() {
        val json = JsonObject("inner" to JsonObject("foo" to true))
        val where = Where(WhereProjection.Field("inner.foo"), Operator.Equal, Value.ValueNumber(54.0))

        val result = json.where(where, false, null)

        assertEquals(JsonObject(), result)
    }

    @Test
    fun `test field from nested object matching`() {
        val json = JsonObject("inner" to JsonObject("foo" to true))
        val where = Where(WhereProjection.Field("inner.foo"), Operator.Equal, Value.ValueBoolean(true))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }
}