package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessThanOperatorTests {
    @Test
    fun `test number less than`() {
        val operator = Operator.LessThan

        assertTrue("lower number less than number", operator.op(5, Value.ValueNumber(10.0), false))
        assertFalse("higher number less than number", operator.op(15, Value.ValueNumber(10.0), false))

        assertFalse("number equal to number", operator.op(5, Value.ValueNumber(5.0), false))

        assertTrue("number less than string", operator.op(5, Value.ValueNumber(6.0), false))
        assertFalse("number equal to string", operator.op(5, Value.ValueNumber(5.0), false))
        assertFalse("number higher than  string", operator.op(5, Value.ValueNumber(4.0), false))
        assertFalse("number to boolean", operator.op(5, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test number less than null`() {
        val operator = Operator.LessThan
        assertTrue("positive to null", operator.op(5, Value.ValueNull, false))
        assertFalse("null to positive", operator.op(null, Value.ValueNumber(5.0), false))

        assertTrue("equal to null", operator.op(0, Value.ValueNull, false))
        assertFalse("null to equal", operator.op(null, Value.ValueNumber(0.0), false))

        assertTrue("negative to null", operator.op(-5, Value.ValueNull, false))
        assertFalse("null to negative", operator.op(null, Value.ValueNumber(-5.0), false))
    }

    @Test
    fun `test boolean less than boolean`() {
        val operator = Operator.LessThan

        assertTrue("false less than true", operator.op(false, Value.ValueBoolean(true), false))
        assertFalse("true less than false", operator.op(true, Value.ValueBoolean(false), false))
        assertFalse("true and true", operator.op(true, Value.ValueBoolean(true), false))
        assertFalse("false and false", operator.op(false, Value.ValueBoolean(false), false))

    }

    @Test
    fun `test boolean less than string`() {
        val operator = Operator.LessThan

        assertFalse("true less than string (true)", operator.op(true, Value.ValueString("true"), false))
        assertFalse("true less than string (false)", operator.op(true, Value.ValueString("false"), false))
        assertFalse("false less than string (true)", operator.op(false, Value.ValueString("true"), false))
        assertFalse("false less than string (false)", operator.op(false, Value.ValueString("true"), false))
        assertFalse("false less than string (empty)", operator.op(false, Value.ValueString(""), false))

    }

    @Test
    fun `test boolean less than number`() {
        val operator = Operator.LessThan

        assertFalse("true less than negative", operator.op(true, Value.ValueNumber(-1.0), false))
        assertFalse("true less than equal", operator.op(true, Value.ValueNumber(0.0), false))
        assertFalse("true less than positive", operator.op(true, Value.ValueNumber(1.0), false))

        assertFalse("false less than negative", operator.op(false, Value.ValueNumber(-1.0), false))
        assertFalse("false less than equal", operator.op(false, Value.ValueNumber(0.0), false))
        assertFalse("false less than positive", operator.op(false, Value.ValueNumber(1.0), false))

    }

    @Test
    fun `test boolean less than null`() {
        val operator = Operator.LessThan

        assertTrue("false less than null", operator.op(false, Value.ValueNull, false))
        assertTrue("true less than null", operator.op(true, Value.ValueNull, false))

        assertFalse("null less than false", operator.op(null, Value.ValueBoolean(false), false))
        assertFalse("null less than true", operator.op(null, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test string less than string`() {
        val operator = Operator.LessThan

        assertTrue("a less than b", operator.op("a", Value.ValueString("b"), false))
        assertFalse("b less than b", operator.op("b", Value.ValueString("b"), false))
        assertFalse("c less than b", operator.op("c", Value.ValueString("b"), false))

        assertTrue("ba less than bb", operator.op("ba", Value.ValueString("bb"), false))
        assertFalse("bb less than bb", operator.op("bb", Value.ValueString("bb"), false))
        assertFalse("bc less than bb", operator.op("bc", Value.ValueString("bb"), false))
    }

    @Test
    fun `test string less than other`() {
        val operator = Operator.LessThan
        assertFalse("number less than string (word)", operator.op(10, Value.ValueString("word"), false))
        assertFalse("number less than string (lower number)", operator.op(10.1, Value.ValueString("10.5"), false))
        assertFalse("number less than string (equal number)", operator.op(10.3, Value.ValueString("10.5"), false))
        assertFalse("number less than string (higher number)", operator.op(10.5, Value.ValueString("10.5"), false))

        assertTrue("string less than null", operator.op("not null", Value.ValueNull, false))
        assertFalse("null less than string", operator.op(null, Value.ValueString("not null"), false))
    }

    @Test
    fun `test null less than null`() {
        val operator = Operator.LessThan

        assertFalse("null less than null", operator.op(null, Value.ValueNull, false))
    }
}