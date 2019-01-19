package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotEqualOperatorTests {
    @Test
    fun `test number equal`() {
        val operator = Operator.NotEqual

        assertFalse("integer and matching number", operator.op(50, Value.ValueNumber(50.0), false))
        assertTrue("integer and not matching whole number ", operator.op(50, Value.ValueNumber(51.0), false))
        assertTrue("integer and not matching number", operator.op(50, Value.ValueNumber(50.5), false))

        assertFalse("double and matching number", operator.op(45.67, Value.ValueNumber(45.67), false))
        assertTrue("double and not matching whole number ", operator.op(45.67, Value.ValueNumber(46.67), false))
        assertTrue("double and not matching number", operator.op(45.67, Value.ValueNumber(45.68), false))

        assertFalse("integer and matching string", operator.op(1, Value.ValueString("1"), false))
        assertTrue("integer and not matching string", operator.op(1, Value.ValueString("3"), false))

        assertFalse("double and matching string", operator.op(103.542, Value.ValueString("103.542"), false))
        assertTrue("double and not matching string", operator.op(103.542, Value.ValueString("900"), false))

        assertTrue("number and boolean (true)", operator.op(1, Value.ValueBoolean(true), false))
        assertTrue("number and boolean (false)", operator.op(1, Value.ValueBoolean(false), false))
    }

    @Test
    fun `test string equal`() {
        val operator = Operator.NotEqual

        assertFalse("string and matching string, matching case - not checking case", operator.op("hello", Value.ValueString("hello"), false))
        assertFalse("string and matching string, matching case - checking case", operator.op("hello", Value.ValueString("hello"), true))

        assertTrue("string and not matching string - not checking case", operator.op("hello", Value.ValueString("hllo"), false))
        assertTrue("string and not matching string - checking case", operator.op("hello", Value.ValueString("hllo"), true))

        assertFalse("string and matching string, not matching case - not checking case", operator.op("hello", Value.ValueString("Hello"), false))
        assertTrue("string and matching string, not matching case - checking case", operator.op("hello", Value.ValueString("Hello"), true))

        assertTrue("string (not boolean) and boolean (true)", operator.op("this word", Value.ValueBoolean(true), false))
        assertFalse("string (boolean) and boolean (true) - not checking case", operator.op("true", Value.ValueBoolean(true), false))
        assertFalse("string (boolean) and boolean (true) - checking case", operator.op("true", Value.ValueBoolean(true), true))

        assertTrue("string (not boolean) and boolean (false)", operator.op("this word", Value.ValueBoolean(false), false))
        assertFalse("string (boolean) and boolean (false) - not checking case", operator.op("false", Value.ValueBoolean(false), false))
        assertFalse("string (boolean) and boolean (false) - checking case", operator.op("false", Value.ValueBoolean(false), true))

        assertFalse("string and matching integer", operator.op("78", Value.ValueNumber(78.0), false))
        assertFalse("string and matching double", operator.op("78.0", Value.ValueNumber(78.0), false))

        assertTrue("string and not matching integer", operator.op("74", Value.ValueNumber(78.0), false))
        assertTrue("string and not matching double", operator.op("74.0", Value.ValueNumber(78.0), false))
    }

    @Test
    fun `test boolean equal`() {
        val operator = Operator.NotEqual

        assertFalse("boolean (true) and matching boolean", operator.op(true, Value.ValueBoolean(true), false))
        assertTrue("boolean (true) and not matching boolean", operator.op(true, Value.ValueBoolean(false), false))

        assertFalse("boolean (false) and matching boolean", operator.op(false, Value.ValueBoolean(false), false))
        assertTrue("boolean (false) and not matching boolean", operator.op(false, Value.ValueBoolean(true), false))

        assertFalse("boolean and matching string", operator.op(true, Value.ValueString("true"), false))
        assertTrue("boolean and not matching string", operator.op(true, Value.ValueString("false"), false))

        assertTrue("boolean and number", operator.op(true, Value.ValueNumber(54.0), false))
    }

    @Test
    fun `test null equal`() {
        val operator = Operator.NotEqual

        assertTrue("null and string", operator.op(null, Value.ValueString("anything"), false))
        assertTrue("null and number", operator.op(null, Value.ValueNumber(98572.24), false))
        assertTrue("null and boolean", operator.op(null, Value.ValueBoolean(false), false))
    }
}