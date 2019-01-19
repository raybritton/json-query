package com.raybritton.jsonquery.unit.operators

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Value
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.parsing.tokens.Operator
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TypeOperatorTests {
    @Test
    fun `test where compare is string`() {
        val operator = Operator.TypeEqual

        assertTrue("string and string", operator.op("this is a string", Value.ValueType(Keyword.STRING), false))
        assertFalse("string and number", operator.op(34, Value.ValueType(Keyword.STRING), false))
        assertFalse("string and boolean", operator.op(false, Value.ValueType(Keyword.STRING), false))
        assertFalse("string and object", operator.op(JsonObject(), Value.ValueType(Keyword.STRING), false))
        assertFalse("string and array", operator.op(JsonArray(), Value.ValueType(Keyword.STRING), false))
        assertFalse("string and null", operator.op(null, Value.ValueType(Keyword.STRING), false))
    }

    @Test
    fun `test where compare is number`() {
        val operator = Operator.TypeEqual

        assertFalse("number and string", operator.op("this is a string", Value.ValueType(Keyword.NUMBER), false))
        assertTrue("number and number", operator.op(34, Value.ValueType(Keyword.NUMBER), false))
        assertFalse("number and boolean", operator.op(false, Value.ValueType(Keyword.NUMBER), false))
        assertFalse("number and object", operator.op(JsonObject(), Value.ValueType(Keyword.NUMBER), false))
        assertFalse("number and array", operator.op(JsonArray(), Value.ValueType(Keyword.NUMBER), false))
        assertFalse("number and null", operator.op(null, Value.ValueType(Keyword.NUMBER), false))
    }

    @Test
    fun `test where compare is boolean`() {
        val operator = Operator.TypeEqual

        assertFalse("boolean and string", operator.op("this is a string", Value.ValueType(Keyword.BOOLEAN), false))
        assertFalse("boolean and number", operator.op(34, Value.ValueType(Keyword.BOOLEAN), false))
        assertTrue("boolean and boolean", operator.op(false, Value.ValueType(Keyword.BOOLEAN), false))
        assertFalse("boolean and object", operator.op(JsonObject(), Value.ValueType(Keyword.BOOLEAN), false))
        assertFalse("boolean and array", operator.op(JsonArray(), Value.ValueType(Keyword.BOOLEAN), false))
        assertFalse("boolean and null", operator.op(null, Value.ValueType(Keyword.BOOLEAN), false))
    }

    @Test
    fun `test where compare is object`() {
        val operator = Operator.TypeEqual

        assertFalse("object and string", operator.op("this is a string", Value.ValueType(Keyword.OBJECT), false))
        assertFalse("object and number", operator.op(34, Value.ValueType(Keyword.OBJECT), false))
        assertFalse("object and boolean", operator.op(false, Value.ValueType(Keyword.OBJECT), false))
        assertTrue("object and object", operator.op(JsonObject(), Value.ValueType(Keyword.OBJECT), false))
        assertFalse("object and array", operator.op(JsonArray(), Value.ValueType(Keyword.OBJECT), false))
        assertFalse("object and null", operator.op(null, Value.ValueType(Keyword.OBJECT), false))
    }

    @Test
    fun `test where compare is array`() {
        val operator = Operator.TypeEqual

        assertFalse("array and string", operator.op("this is a string", Value.ValueType(Keyword.ARRAY), false))
        assertFalse("array and number", operator.op(34, Value.ValueType(Keyword.ARRAY), false))
        assertFalse("array and boolean", operator.op(false, Value.ValueType(Keyword.ARRAY), false))
        assertFalse("array and object", operator.op(JsonObject(), Value.ValueType(Keyword.ARRAY), false))
        assertTrue("array and array", operator.op(JsonArray(), Value.ValueType(Keyword.ARRAY), false))
        assertFalse("array and null", operator.op(null, Value.ValueType(Keyword.ARRAY), false))
    }

    @Test
    fun `test where compare is null`() {
        val operator = Operator.TypeEqual

        assertFalse("null and string", operator.op("this is a string", Value.ValueType(Keyword.NULL), false))
        assertFalse("null and number", operator.op(34, Value.ValueType(Keyword.NULL), false))
        assertFalse("null and boolean", operator.op(false, Value.ValueType(Keyword.NULL), false))
        assertFalse("null and object", operator.op(JsonObject(), Value.ValueType(Keyword.NULL), false))
        assertFalse("null and array", operator.op(JsonArray(), Value.ValueType(Keyword.NULL), false))
        assertTrue("null and null", operator.op(null, Value.ValueType(Keyword.NULL), false))
    }
}