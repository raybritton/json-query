package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContainsOperatorTests {
    @Test
    fun `test contains string in string`() {
        val operator = Operator.Contains

        assertTrue("string in string - not checking case", operator.op("this is a string", Value.ValueString("this"), false))
        assertFalse("string not in string - not checking case", operator.op("this is a string", Value.ValueString("not"), false))

        assertTrue("string in string - checking case", operator.op("this is a string", Value.ValueString("this"), true))
        assertFalse("string not in string - checking case", operator.op("this is a string", Value.ValueString("not"), true))

        assertFalse("string in string (diff case) - checking case", operator.op("this is a string", Value.ValueString("THIS"), true))
        assertFalse("string not in string (diff case) - checking case", operator.op("this is a string", Value.ValueString("NOT"), true))

        assertTrue("string in string (diff case) - not checking case", operator.op("this is a string", Value.ValueString("THIS"), false))
        assertFalse("string not in string (diff case) - not checking case", operator.op("this is a string", Value.ValueString("NOT"), false))

        assertTrue("empty string in string", operator.op("this is a string", Value.ValueString(""), false))

        assertTrue("whole string match", operator.op("string", Value.ValueString("string"), false))
    }

    @Test
    fun `test contains other in string`() {
        val operator = Operator.Contains

        assertFalse("number in string", operator.op("this is a 10", Value.ValueNumber(10.0), false))
        assertFalse("number not in string", operator.op("this is a 10", Value.ValueNumber(11.0), false))
        assertFalse("boolean in string", operator.op("this is a true", Value.ValueBoolean(true), false))
        assertFalse("boolean not in string", operator.op("this is a false", Value.ValueBoolean(false), false))
        assertFalse("null in string", operator.op("this is a false", Value.ValueNull, false))
    }

    @Test
    fun `test contains string in array`() {
        val operator = Operator.Contains

        assertTrue("string in array - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("this"), false))
        assertFalse("string not in array - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("not"), false))

        assertTrue("string in array - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("this"), true))
        assertFalse("string not in array - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("not"), true))

        assertTrue("string in array (diff case) - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("THIS"), false))
        assertFalse("string not in array (diff case) - not checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("NOT"), false))

        assertFalse("string in array (diff case) - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("THIS"), true))
        assertFalse("string not in array (diff case) - checking case", operator.op(JsonArray("this", "is", "string"), Value.ValueString("NOT"), true))
    }

    @Test
    fun `test contains other in array`() {
        val operator = Operator.Contains

        assertTrue("integer in array", operator.op(JsonArray(1, 2, 3), Value.ValueNumber(1.0), false))
        assertFalse("integer not in array", operator.op(JsonArray(1, 2, 3), Value.ValueNumber(4.0), false))

        assertTrue("double in array", operator.op(JsonArray(1.3, 2.2, 3.1), Value.ValueNumber(1.3), false))
        assertFalse("double not in array", operator.op(JsonArray(1.3, 2.2, 3.1), Value.ValueNumber(1.5), false))

        assertTrue("boolean in array", operator.op(JsonArray(true, false), Value.ValueBoolean(true), false))
        assertFalse("boolean not in array", operator.op(JsonArray(true), Value.ValueBoolean(false), false))

        assertTrue("null in array", operator.op(JsonArray(true, null), Value.ValueNull, false))
        assertFalse("null not in array", operator.op(JsonArray("not null"), Value.ValueNull, false))
    }

    @Test
    fun `test contains in object`() {
        val operator = Operator.Contains

        assertTrue("matching key, matching case - not checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("foo"), false))
        assertTrue("matching key, matching case - checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("foo"), true))

        assertTrue("matching key, not matching case - not checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("FOO"), false))
        assertFalse("matching key, not matching case - checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("FOO"), true))

        assertFalse("not matching key - not checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("bar"), false))
        assertFalse("not matching key - checking case", operator.op(JsonObject("foo" to "bar"), Value.ValueString("bar"), true))

        assertTrue("boolean key", operator.op(JsonObject("true" to "bar"), Value.ValueBoolean(true), false))

        assertTrue("number key", operator.op(JsonObject("123" to "bar"), Value.ValueNumber(123.0), false))
    }
}