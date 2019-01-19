package com.raybritton.jsonquery.unit.filtering

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.models.Where
import com.raybritton.jsonquery.models.WhereProjection
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.tools.where
import org.junit.Assert.assertEquals
import org.junit.Test

class ArrayElementTests {
    @Test
    fun `test array where equal single value(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueNumber(400.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(400), result)
    }

    @Test
    fun `test array where equal multiple values(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueNumber(2.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(2, 2), result)
    }

    @Test
    fun `test array where equal no values(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueNumber(14.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(), result)
    }

    @Test
    fun `test array where not equal multiple values(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.NotEqual, Value.ValueNumber(14.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(-1, 9, 2, 3, 400, 2), result)
    }

    @Test
    fun `test array where not equal multiple values not matching(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.NotEqual, Value.ValueNumber(-1.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(9, 2, 3, 400, 2), result)
    }

    @Test
    fun `test array where greater than(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.GreaterThan, Value.ValueNumber(9.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(400), result)
    }

    @Test
    fun `test array where greater than or equal(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.GreaterThanOrEqual, Value.ValueNumber(9.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(9, 400), result)
    }

    @Test
    fun `test array where less than(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.LessThan, Value.ValueNumber(2.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(-1), result)
    }

    @Test
    fun `test array where less than or equal(numbers)`() {
        val json = JsonArray(-1, 9, 2, 3, 400, 2)

        val where = Where(WhereProjection.Element, Operator.LessThanOrEqual, Value.ValueNumber(2.0))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(-1, 2, 2), result)
    }

    @Test
    fun `test array where equal single value(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueString("a"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("a"), result)
    }

    @Test
    fun `test array where equal mutliple values(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueString("c"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("c", "c"), result)
    }

    @Test
    fun `test array where equal no values(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueString("delta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(), result)
    }

    @Test
    fun `test array where not equal multiple values(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.NotEqual, Value.ValueString("beta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("a", "c", "c"), result)
    }

    @Test
    fun `test array where not equal multiple values not matching(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.NotEqual, Value.ValueString("delta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("a", "beta", "c", "c"), result)
    }

    @Test
    fun `test array where greater than(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.GreaterThan, Value.ValueString("beta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("c", "c"), result)
    }

    @Test
    fun `test array where greater than or equal(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.GreaterThanOrEqual, Value.ValueString("beta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("beta", "c", "c"), result)
    }

    @Test
    fun `test array where less than(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.LessThan, Value.ValueString("beta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("a"), result)
    }

    @Test
    fun `test array where less than or equal(strings)`() {
        val json = JsonArray("a", "beta", "c", "c")

        val where = Where(WhereProjection.Element, Operator.LessThanOrEqual, Value.ValueString("beta"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("a", "beta"), result)
    }

    @Test
    fun `test empty array`() {
        val json = JsonArray()

        val where = Where(WhereProjection.Element, Operator.LessThanOrEqual, Value.ValueBoolean(true))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(), result)
    }

    @Test
    fun `test boolean array matching (true)`() {
        val json = JsonArray(true, false, null)

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueBoolean(true))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(true), result)
    }

    @Test
    fun `test boolean array matching (false)`() {
        val json = JsonArray(true, false, null)

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueBoolean(false))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(false), result)
    }

    @Test
    fun `test number array with string value`() {
        val json = JsonArray(10, 20)

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueString("10"))

        val result = json.where(where, false, null)

        assertEquals(JsonArray(10), result)
    }

    @Test
    fun `test string array with number value`() {
        val json = JsonArray("1.433", "50")

        val where = Where(WhereProjection.Element, Operator.Equal, Value.ValueNumber(1.433))

        val result = json.where(where, false, null)

        assertEquals(JsonArray("1.433"), result)
    }

    @Test
    fun `test numbers offset`() {
        val json = JsonArray(10, 20, 30, 40)

        val where = Where(WhereProjection.Element, Operator.GreaterThan, Value.ValueNumber(0.0))

        val result1 = json.where(where, false, 0)
        val result2 = json.where(where, false, 1)
        val result3 = json.where(where, false, 3)
        val result4 = json.where(where, false, null)

        assertEquals(JsonArray(), result1)
        assertEquals(JsonArray(10), result2)
        assertEquals(JsonArray(10, 20, 30), result3)
        assertEquals(JsonArray(10, 20, 30, 40), result4)


    }
}
