package com.raybritton.jsonquery.unit

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.filter
import org.junit.Assert.assertEquals
import org.junit.Test

class FilteringTest {
        @Test
    fun testSpecificSingleQuery() {
        val jsonStr = "{'a':1,'b':2,'c':3}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.SPECIFIC, listOf("a"))
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", mapOf("a" to 1.0), output)
    }

    @Test
    fun testSpecificDoubleQuery() {
        val jsonStr = "{'a':1,'b':2,'c':3}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.SPECIFIC, listOf("a", "b"))
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", mapOf("a" to 1.0, "b" to 2.0), output)
    }

    @Test
    fun testKeysQuery() {
        val jsonStr = "{'a':1,'b':2,'c':3, 'd':[6,7,8]}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.KEYS)
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", listOf("a", "b", "c", "d"), output)
    }

    @Test
    fun testValuesQuery() {
        val jsonStr = "{'a':1,'b':2,'c':3, 'd':[6,7,8]}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.VALUES)
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", listOf(1.0, 2.0, 3.0, 6.0, 7.0, 8.0), output)
    }

    @Test
    fun testNestedKeysQuery() {
        val jsonStr = "{'a':1,'b':2,'c':{'ia':43,'ib':true, 'id':[9,10,11]}, 'd':[6,7,8]}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.KEYS)
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", listOf("a", "b", "c", "ia", "ib", "id", "d"), output)
    }

    @Test
    fun testNestedValuesQuery() {
        val jsonStr = "{'a':1,'b':2,'c':{'ia':43,'ib':true, 'id':[9,10,11]}, 'd':[6,7,8]}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.VALUES)
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", listOf(1.0, 2.0, 43.0, true, 9.0, 10.0, 11.0, 6.0, 7.0, 8.0), output)
    }

    @Test
    fun testDoubleNestedKeysQuery() {
        val jsonStr = "{'a':1,'b':2,'c':{'ia':43,'ib':true, 'ic':{'even':'deeper','ed':[90,91]}, 'id':[9,10,11]}, 'd':[6,7,8]}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.KEYS)
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", listOf("a", "b", "c", "ia", "ib", "ic", "even", "ed", "id", "d"), output)
    }

    @Test
    fun testDoubleNestedValuesQuery() {
        val jsonStr = "{'a':1,'b':2,'c':{'ia':43,'ib':true, 'ic':{'even':'deeper','ed':[90,91]}, 'id':[9,10,11]}, 'd':[6,7,8]}"
        val query = Query(Query.Method.SELECT, ".", Query.TargetExtra.VALUES)
        val json = Gson().fromJson<Any>(jsonStr, Any::class.java)

        val output = json.filter(query)

        assertEquals("", listOf(1.0, 2.0, 43.0, true, "deeper", 90.0, 91.0, 9.0, 10.0, 11.0, 6.0, 7.0, 8.0), output)
    }
}