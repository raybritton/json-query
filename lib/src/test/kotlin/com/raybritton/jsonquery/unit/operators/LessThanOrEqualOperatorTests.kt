package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessThanOrEqualOperatorTests {
    @Test
    fun `test number less than or equal`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("lower number less than number", operator.op(5, Value.ValueNumber(10.0), false))
        assertFalse("higher number less than number", operator.op(15, Value.ValueNumber(10.0), false))

        assertTrue("number equal to number", operator.op(5, Value.ValueNumber(5.0), false))

        assertTrue("number less than string", operator.op(5, Value.ValueNumber(6.0), false))
        assertTrue("number equal to string", operator.op(5, Value.ValueNumber(5.0), false))
        assertFalse("number higher than  string", operator.op(5, Value.ValueNumber(4.0), false))
        assertTrue("number to boolean", operator.op(5, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test number less than or equal null`() {
        val operator = Operator.LessThanOrEqual
        assertTrue("positive to null", operator.op(5, Value.ValueNull, false))
        assertFalse("null to positive", operator.op(null, Value.ValueNumber(5.0), false))

        assertTrue("equal to null", operator.op(0, Value.ValueNull, false))
        assertFalse("null to equal", operator.op(null, Value.ValueNumber(0.0), false))

        assertTrue("negative to null", operator.op(-5, Value.ValueNull, false))
        assertFalse("null to negative", operator.op(null, Value.ValueNumber(-5.0), false))
    }

    @Test
    fun `test boolean less than or equal boolean`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("false less than true", operator.op(false, Value.ValueBoolean(true), false))
        assertFalse("true less than false", operator.op(true, Value.ValueBoolean(false), false))
        assertTrue("true and true", operator.op(true, Value.ValueBoolean(true), false))
        assertTrue("false and false", operator.op(false, Value.ValueBoolean(false), false))

    }

    @Test
    fun `test boolean less than or equal string`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("true less than or equal string (true)", operator.op(true, Value.ValueString("true"), false))
        assertTrue("true less than string (false)", operator.op(true, Value.ValueString("false"), false))
        assertTrue("false less than string (true)", operator.op(false, Value.ValueString("true"), false))
        assertTrue("false less than or equal string (false)", operator.op(false, Value.ValueString("true"), false))
        assertTrue("false less than string (empty)", operator.op(false, Value.ValueString(""), false))
    }

    @Test
    fun `test boolean less than or equal number`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("true less than negative", operator.op(true, Value.ValueNumber(-1.0), false))
        assertTrue("true less than equal", operator.op(true, Value.ValueNumber(0.0), false))
        assertTrue("true less than positive", operator.op(true, Value.ValueNumber(1.0), false))

        assertTrue("false less than negative", operator.op(false, Value.ValueNumber(-1.0), false))
        assertTrue("false less than equal", operator.op(false, Value.ValueNumber(0.0), false))
        assertTrue("false less than positive", operator.op(false, Value.ValueNumber(1.0), false))
    }

    @Test
    fun `test boolean less than or equal null`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("false less than null", operator.op(false, Value.ValueNull, false))
        assertTrue("true less than null", operator.op(true, Value.ValueNull, false))

        assertFalse("null less than false", operator.op(null, Value.ValueBoolean(false), false))
        assertFalse("null less than true", operator.op(null, Value.ValueBoolean(true), false))
    }

    @Test
    fun `test string less than or equal string`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("char less than char", operator.op("a", Value.ValueString("b"), false))
        assertTrue("char equal to char", operator.op("b", Value.ValueString("b"), false))
        assertFalse("char greater than char", operator.op("c", Value.ValueString("b"), false))

        assertTrue("string less than string", operator.op("ba", Value.ValueString("bb"), false))
        assertTrue("string equal to string", operator.op("bb", Value.ValueString("bb"), false))
        assertFalse("string greater than string", operator.op("bc", Value.ValueString("bb"), false))
    }

    @Test
    fun `test string less than or equal other`() {
        val operator = Operator.LessThanOrEqual
        assertTrue("number less than string (word)", operator.op(10, Value.ValueString("word"), false))
        assertTrue("number less than string (lower number)", operator.op(10.1, Value.ValueString("10.5"), false))
        assertTrue("number less than string (equal number)", operator.op(10.3, Value.ValueString("10.5"), false))
        assertTrue("number less than string (higher number)", operator.op(10.5, Value.ValueString("10.5"), false))

        assertTrue("string less than null", operator.op("not null", Value.ValueNull, false))
        assertFalse("null less than string", operator.op(null, Value.ValueString("not null"), false))
    }

    @Test
    fun `test null less than or equal null`() {
        val operator = Operator.LessThanOrEqual

        assertTrue("null less than null", operator.op(null, Value.ValueNull, false))
    }
}