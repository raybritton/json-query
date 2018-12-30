package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotContainsOperatorTests {
    @Test
    fun `test contains in string`() {
        val operator = Operator.NotContains

        assertFalse("string in string - not checking case", operator.op("this is a string", Value.ValueString("this"), false))
        assertTrue("string not in string - not checking case", operator.op("this is a string", Value.ValueString("not"), false))

        assertFalse("string in string - checking case", operator.op("this is a string", Value.ValueString("this"), true))
        assertTrue("string not in string - checking case", operator.op("this is a string", Value.ValueString("not"), true))

        assertTrue("string in string (diff case) - checking case", operator.op("this is a string", Value.ValueString("THIS"), true))
        assertTrue("string not in string (diff case) - checking case", operator.op("this is a string", Value.ValueString("NOT"), true))

        assertFalse("string in string (diff case) - not checking case", operator.op("this is a string", Value.ValueString("THIS"), false))
        assertTrue("string not in string (diff case) - not checking case", operator.op("this is a string", Value.ValueString("NOT"), false))

        assertFalse("whole string match", operator.op("string", Value.ValueString("string"), false))
    }

    @Test
    fun `test contains other in string`() {
        val operator = Operator.NotContains

        assertTrue("number in string", operator.op("this is a 10", Value.ValueNumber(10.0), false))
        assertTrue("number not in string", operator.op("this is a 10", Value.ValueNumber(11.0), false))
        assertTrue("boolean in string", operator.op("this is a true", Value.ValueBoolean(true), false))
        assertTrue("boolean not in string", operator.op("this is a false", Value.ValueBoolean(false), false))
        assertTrue("null in string", operator.op("this is a false", Value.ValueNull, false))
    }

    @Test
    fun `test contains string in array`() {
        val operator = Operator.NotContains

        assertFalse("string in array - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("this"), false))
        assertTrue("string not in array - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("not"), false))

        assertFalse("string in array - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("this"), true))
        assertTrue("string not in array - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("not"), true))

        assertFalse("string in array (diff case) - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("THIS"), false))
        assertTrue("string not in array (diff case) - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("NOT"), false))

        assertTrue("string in array (diff case) - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("THIS"), true))
        assertTrue("string not in array (diff case) - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("NOT"), true))
    }

    @Test
    fun `test contains other in array`() {
        val operator = Operator.NotContains

        assertFalse("integer in array", operator.op(JsonArray(1, 2, 3), Value.ValueNumber(1.0), false))
        assertTrue("integer not in array", operator.op(JsonArray(1, 2, 3), Value.ValueNumber(4.0), false))

        assertFalse("double in array", operator.op(JsonArray(1.3, 2.2, 3.1), Value.ValueNumber(1.3), false))
        assertTrue("double not in array", operator.op(JsonArray(1.3, 2.2, 3.1), Value.ValueNumber(1.5), false))

        assertFalse("boolean in array", operator.op(JsonArray(true, false), Value.ValueBoolean(true), false))
        assertTrue("boolean not in array", operator.op(JsonArray(true), Value.ValueBoolean(false), false))

        assertFalse("null in array", operator.op(JsonArray(true, null), Value.ValueNull, false))
        assertTrue("null not in array", operator.op(JsonArray("not null"), Value.ValueNull, false))
    }

    @Test
    fun `test contains in object`() {
        val operator = Operator.NotContains

        assertFalse("matching key, matching case - not checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("foo"), false))
        assertFalse("matching key, matching case - checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("foo"), true))

        assertFalse("matching key, not matching case - not checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("FOO"), false))
        assertTrue("matching key, not matching case - checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("FOO"), true))

        assertTrue("not matching key - not checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("bar"), false))
        assertTrue("not matching key - checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("bar"), true))

        assertFalse("boolean key", operator.op(JsonObject("true" to "bar"), Value.ValueBoolean(true), false))

        assertFalse("number key", operator.op(JsonObject("123" to "bar"), Value.ValueNumber(123.0), false))
    }
}