package com.raybritton.jsonquery.unit.filtering

import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.tools.where
import org.junit.Assert.assertEquals
import org.junit.Test

class NestedTests {
    @Test
    fun `test array in object matching`() {
        val json = JsonObject("array" to JsonArray(1, 2, 3))
        val where = Where(WhereProjection.Field("array"), Operator.Contains, Value.ValueNumber(1.0))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }

    @Test
    fun `test array in object not matching`() {
        val json = JsonObject("array" to JsonArray(1, 2, 3))
        val where = Where(WhereProjection.Field("array"), Operator.NotContains, Value.ValueNumber(1.0))

        val result = json.where(where, false, null)

        assertEquals(JsonObject(), result)
    }

    @Test
    fun `test 2 objects in array both matching`() {
        val json = JsonArray(JsonObject("foo" to "bar"), JsonObject("foo" to "baz"))
        val where = Where(WhereProjection.Field("foo"), Operator.Contains, Value.ValueString("ba"))

        val result = json.where(where, false, null)

        assertEquals(json, result)
    }

    @Test
    fun `test 2 objects in array one matching`() {
        val json = JsonArray(JsonObject("foo" to "bar"), JsonObject("foo" to "baz"))
        val where = Where(WhereProjection.Field("foo"), Operator.Contains, Value.ValueString("bar"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(JsonObject("foo" to "bar")), result)
    }

    @Test
    fun `test 2 objects in array none matching`() {
        val json = JsonArray(JsonObject("foo" to "bar"), JsonObject("foo" to "baz"), "not an object", JsonObject("test" to 1))
        val where = Where(WhereProjection.Field("foo"), Operator.Contains, Value.ValueString("nothing"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(), result)
    }

    @Test
    fun `test fields nested in objects in array some matching`() {
        val json = JsonArray(JsonObject("foo" to JsonObject("bar" to true)), JsonObject("foo" to JsonObject("bar" to false)), JsonObject("foo" to JsonObject("bar" to null)))
        val where = Where(WhereProjection.Field("foo.bar"), Operator.NotEqual, Value.ValueNull)

        val result = json.where(where, false, null)

        assertEquals(JsonArray(JsonObject("foo" to JsonObject("bar" to true)), JsonObject("foo" to JsonObject("bar" to false))), result)
    }

    @Test
    fun `test fields nested in objects in array one matching`() {
        val json = JsonArray(JsonObject("foo" to JsonObject("bar" to true)), JsonObject("foo" to JsonObject("bar" to false)), JsonObject("foo" to JsonObject("bar" to null)))
        val where = Where(WhereProjection.Field("foo.bar"), Operator.Equal, Value.ValueBoolean(true))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(JsonObject("foo" to JsonObject("bar" to true))), result)
    }
}