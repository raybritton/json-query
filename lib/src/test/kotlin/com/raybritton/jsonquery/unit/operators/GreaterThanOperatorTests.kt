package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GreaterThanOperatorTests {
    @Test
    fun `test number greater than`() {
        val operator = Operator.GreaterThan

        assertFalse("lower number greater than number", operator.op(5, Value.ValueNumber(10.0), false))
        assertTrue("higher number greater than number", operator.op(15, Value.ValueNumber(10.0), false))

        assertFalse("number equal to number", operator.op(5, Value.ValueNumber(5.0), false))

        assertFalse("number greater than string", operator.op(5, Value.ValueNumber(6.0), false))
        assertFalse("number equal to string", operator.op(5, Value.ValueNumber(5.0), false))
        assertTrue("number higher than  string", operator.op(5, Value.ValueNumber(4.0), false))
        assertFalse("number to boolean", operator.op(5, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test number greater than null`() {
        val operator = Operator.GreaterThan
        assertFalse("positive to null", operator.op(5, Value.ValueNull, false))
        assertTrue("null to positive", operator.op(null, Value.ValueNumber(5.0), false))

        assertFalse("equal to null", operator.op(0, Value.ValueNull, false))
        assertTrue("null to equal", operator.op(null, Value.ValueNumber(0.0), false))

        assertFalse("negative to null", operator.op(-5, Value.ValueNull, false))
        assertTrue("null to negative", operator.op(null, Value.ValueNumber(-5.0), false))
    }

    @Test
    fun `test boolean greater than boolean`() {
        val operator = Operator.GreaterThan

        assertFalse("false greater than true", operator.op(false, Value.ValueBoolean(true), false))
        assertTrue("true greater than false", operator.op(true, Value.ValueBoolean(false), false))
        assertFalse("true and true", operator.op(true, Value.ValueBoolean(true), false))
        assertFalse("false and false", operator.op(false, Value.ValueBoolean(false), false))

    }

    @Test
    fun `test boolean greater than string`() {
        val operator = Operator.GreaterThan

        assertFalse("true greater than string (true)", operator.op(true, Value.ValueString("true"), false))
        assertFalse("true greater than string (false)", operator.op(true, Value.ValueString("false"), false))
        assertFalse("false greater than string (true)", operator.op(false, Value.ValueString("true"), false))
        assertFalse("false greater than string (false)", operator.op(false, Value.ValueString("true"), false))
        assertFalse("false greater than string (empty)", operator.op(false, Value.ValueString(""), false))
    }

    @Test
    fun `test boolean greater than number`() {
        val operator = Operator.GreaterThan

        assertFalse("true greater than negative", operator.op(true, Value.ValueNumber(-1.0), false))
        assertFalse("true greater than equal", operator.op(true, Value.ValueNumber(0.0), false))
        assertFalse("true greater than positive", operator.op(true, Value.ValueNumber(1.0), false))

        assertFalse("false greater than negative", operator.op(false, Value.ValueNumber(-1.0), false))
        assertFalse("false greater than equal", operator.op(false, Value.ValueNumber(0.0), false))
        assertFalse("false greater than positive", operator.op(false, Value.ValueNumber(1.0), false))
    }

    @Test
    fun `test boolean greater than null`() {
        val operator = Operator.GreaterThan

        assertFalse("false greater than null", operator.op(false, Value.ValueNull, false))
        assertFalse("true greater than null", operator.op(true, Value.ValueNull, false))

        assertTrue("null greater than false", operator.op(null, Value.ValueBoolean(false), false))
        assertTrue("null greater than true", operator.op(null, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test string greater than string`() {
        val operator = Operator.GreaterThan

        assertFalse("char less than char", operator.op("a", Value.ValueString("b"), false))
        assertFalse("char equal to char", operator.op("b", Value.ValueString("b"), false))
        assertTrue("char greater than char", operator.op("c", Value.ValueString("b"), false))

        assertFalse("string less than string", operator.op("ba", Value.ValueString("bb"), false))
        assertFalse("string equal to string", operator.op("bb", Value.ValueString("bb"), false))
        assertTrue("string greater than string", operator.op("bc", Value.ValueString("bb"), false))
    }

    @Test
    fun `test string greater than other`() {
        val operator = Operator.GreaterThan
        assertFalse("number greater than string (word)", operator.op(10, Value.ValueString("word"), false))
        assertFalse("number greater than string (lower number)", operator.op(10.1, Value.ValueString("10.5"), false))
        assertFalse("number greater than string (equal number)", operator.op(10.3, Value.ValueString("10.5"), false))
        assertFalse("number greater than string (higher number)", operator.op(10.5, Value.ValueString("10.5"), false))

        assertFalse("string greater than null", operator.op("not null", Value.ValueNull, false))
        assertTrue("null greater than string", operator.op(null, Value.ValueString("not null"), false))
    }

    @Test
    fun `test null greater than null`() {
        val operator = Operator.GreaterThan

        assertFalse("null greater than null", operator.op(null, Value.ValueNull, false))
    }
}