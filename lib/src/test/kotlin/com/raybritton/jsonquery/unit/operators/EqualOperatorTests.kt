package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EqualOperatorTests {
    @Test
    fun `test number equal`() {
        val operator = Operator.Equal

        assertTrue("integer and matching number", operator.op(50, Value.ValueNumber(50.0), false))
        assertFalse("integer and not matching whole number ", operator.op(50, Value.ValueNumber(51.0), false))
        assertFalse("integer and not matching number", operator.op(50, Value.ValueNumber(50.5), false))

        assertTrue("double and matching number", operator.op(45.67, Value.ValueNumber(45.67), false))
        assertFalse("double and not matching whole number ", operator.op(45.67, Value.ValueNumber(46.67), false))
        assertFalse("double and not matching number", operator.op(45.67, Value.ValueNumber(45.68), false))

        assertTrue("integer and matching string", operator.op(1, Value.ValueString("1"), false))
        assertFalse("integer and not matching string", operator.op(1, Value.ValueString("3"), false))

        assertTrue("double and matching string", operator.op(103.542, Value.ValueString("103.542"), false))
        assertFalse("double and not matching string", operator.op(103.542, Value.ValueString("900"), false))

        assertFalse("number and boolean (true)", operator.op(1, Value.ValueBoolean(true), false))
        assertFalse("number and boolean (false)", operator.op(1, Value.ValueBoolean(false), false))
    }

    @Test
    fun `test string equal`() {
        val operator = Operator.Equal

        assertTrue("string and matching string, matching case - not checking case", operator.op("hello", Value.ValueString("hello"), false))
        assertTrue("string and matching string, matching case - checking case", operator.op("hello", Value.ValueString("hello"), true))

        assertFalse("string and not matching string - not checking case", operator.op("hello", Value.ValueString("hllo"), false))
        assertFalse("string and not matching string - checking case", operator.op("hello", Value.ValueString("hllo"), true))

        assertTrue("string and matching string, not matching case - not checking case", operator.op("hello", Value.ValueString("Hello"), false))
        assertFalse("string and matching string, not matching case - checking case", operator.op("hello", Value.ValueString("Hello"), true))

        assertFalse("string (not boolean) and boolean (true)", operator.op("this word", Value.ValueBoolean(true), false))
        assertTrue("string (boolean) and boolean (true) - not checking case", operator.op("true", Value.ValueBoolean(true), false))
        assertTrue("string (boolean) and boolean (true) - checking case", operator.op("true", Value.ValueBoolean(true), true))

        assertFalse("string (not boolean) and boolean (false)", operator.op("this word", Value.ValueBoolean(false), false))
        assertTrue("string (boolean) and boolean (false) - not checking case", operator.op("false", Value.ValueBoolean(false), false))
        assertTrue("string (boolean) and boolean (false) - checking case", operator.op("false", Value.ValueBoolean(false), true))

        assertTrue("string and matching integer", operator.op("78", Value.ValueNumber(78.0), false))
        assertTrue("string and matching double", operator.op("78.0", Value.ValueNumber(78.0), false))

        assertFalse("string and not matching integer", operator.op("74", Value.ValueNumber(78.0), false))
        assertFalse("string and not matching double", operator.op("74.0", Value.ValueNumber(78.0), false))
    }

    @Test
    fun `test boolean equal`() {
        val operator = Operator.Equal

        assertTrue("boolean (true) and matching boolean", operator.op(true, Value.ValueBoolean(true), false))
        assertFalse("boolean (true) and not matching boolean", operator.op(true, Value.ValueBoolean(false), false))

        assertTrue("boolean (false) and matching boolean", operator.op(false, Value.ValueBoolean(false), false))
        assertFalse("boolean (false) and not matching boolean", operator.op(false, Value.ValueBoolean(true), false))

        assertTrue("boolean and matching string", operator.op(true, Value.ValueString("true"), false))
        assertFalse("boolean and not matching string", operator.op(true, Value.ValueString("false"), false))

        assertFalse("boolean and number", operator.op(true, Value.ValueNumber(54.0), false))
    }

    @Test
    fun `test null equal`() {
        val operator = Operator.Equal

        assertFalse("null and string", operator.op(null, Value.ValueString("anything"), false))
        assertFalse("null and number", operator.op(null, Value.ValueNumber(98572.24), false))
        assertFalse("null and boolean", operator.op(null, Value.ValueBoolean(false), false))
    }
}