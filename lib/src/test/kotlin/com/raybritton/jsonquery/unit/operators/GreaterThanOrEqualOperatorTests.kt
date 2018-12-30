package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GreaterThanOrEqualOperatorTests {
    @Test
    fun `test number greater than or equal`() {
        val operator = Operator.GreaterThanOrEqual

        assertFalse("lower number greater than number", operator.op(5, Value.ValueNumber(10.0), false))
        assertTrue("higher number greater than number", operator.op(15, Value.ValueNumber(10.0), false))

        assertTrue("number equal to number", operator.op(5, Value.ValueNumber(5.0), false))

        assertFalse("number greater than string", operator.op(5, Value.ValueNumber(6.0), false))
        assertTrue("number equal to string", operator.op(5, Value.ValueNumber(5.0), false))
        assertTrue("number higher than  string", operator.op(5, Value.ValueNumber(4.0), false))
        assertTrue("number to boolean", operator.op(5, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test number greater than or equal null`() {
        val operator = Operator.GreaterThanOrEqual
        assertFalse("positive to null", operator.op(5, Value.ValueNull, false))
        assertTrue("null to positive", operator.op(null, Value.ValueNumber(5.0), false))

        assertFalse("equal to null", operator.op(0, Value.ValueNull, false))
        assertTrue("null to equal", operator.op(null, Value.ValueNumber(0.0), false))

        assertFalse("negative to null", operator.op(-5, Value.ValueNull, false))
        assertTrue("null to negative", operator.op(null, Value.ValueNumber(-5.0), false))
    }

    @Test
    fun `test boolean greater than or equal boolean`() {
        val operator = Operator.GreaterThanOrEqual

        assertFalse("false greater than true", operator.op(false, Value.ValueBoolean(true), false))
        assertTrue("true greater than false", operator.op(true, Value.ValueBoolean(false), false))
        assertTrue("true and true", operator.op(true, Value.ValueBoolean(true), false))
        assertTrue("false and false", operator.op(false, Value.ValueBoolean(false), false))

    }

    @Test
    fun `test boolean greater than or equal string`() {
        val operator = Operator.GreaterThanOrEqual

        assertTrue("true greater than string (true)", operator.op(true, Value.ValueString("true"), false))
        assertTrue("true greater than string (false)", operator.op(true, Value.ValueString("false"), false))
        assertTrue("false greater than string (true)", operator.op(false, Value.ValueString("true"), false))
        assertTrue("false greater than string (false)", operator.op(false, Value.ValueString("true"), false))
        assertTrue("false greater than string (empty)", operator.op(false, Value.ValueString(""), false))
    }

    @Test
    fun `test boolean greater than or equal number`() {
        val operator = Operator.GreaterThanOrEqual

        assertTrue("true greater than negative", operator.op(true, Value.ValueNumber(-1.0), false))
        assertTrue("true greater than equal", operator.op(true, Value.ValueNumber(0.0), false))
        assertTrue("true greater than positive", operator.op(true, Value.ValueNumber(1.0), false))

        assertTrue("false greater than negative", operator.op(false, Value.ValueNumber(-1.0), false))
        assertTrue("false greater than equal", operator.op(false, Value.ValueNumber(0.0), false))
        assertTrue("false greater than positive", operator.op(false, Value.ValueNumber(1.0), false))
    }

    @Test
    fun `test boolean greater than or equal null`() {
        val operator = Operator.GreaterThanOrEqual

        assertFalse("false greater than null", operator.op(false, Value.ValueNull, false))
        assertFalse("true greater than null", operator.op(true, Value.ValueNull, false))

        assertTrue("null greater than false", operator.op(null, Value.ValueBoolean(false), false))
        assertTrue("null greater than true", operator.op(null, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test string greater than or equal string`() {
        val operator = Operator.GreaterThanOrEqual

        assertFalse("char less than char", operator.op("a", Value.ValueString("b"), false))
        assertTrue("char equal to char", operator.op("b", Value.ValueString("b"), false))
        assertTrue("char greater than char", operator.op("c", Value.ValueString("b"), false))

        assertFalse("string less than string", operator.op("ba", Value.ValueString("bb"), false))
        assertTrue("string equal to string", operator.op("bb", Value.ValueString("bb"), false))
        assertTrue("string greater than string", operator.op("bc", Value.ValueString("bb"), false))
    }

    @Test
    fun `test string greater than or equal other`() {
        val operator = Operator.GreaterThanOrEqual
        assertTrue("number greater than or equal string (word)", operator.op(10, Value.ValueString("word"), false))
        assertTrue("number greater than or equal string (lower number)", operator.op(10.1, Value.ValueString("10.5"), false))
        assertTrue("number greater than or equal string (equal number)", operator.op(10.3, Value.ValueString("10.5"), false))
        assertTrue("number greater than or equal string (higher number)", operator.op(10.5, Value.ValueString("10.5"), false))

        assertFalse("string greater than or equal null", operator.op("not null", Value.ValueNull, false))
        assertTrue("null greater than or equal string", operator.op(null, Value.ValueString("not null"), false))
    }

    @Test
    fun `test null greater than or equal null`() {
        val operator = Operator.GreaterThanOrEqual

        assertTrue("null greater than null", operator.op(null, Value.ValueNull, false))
    }
}