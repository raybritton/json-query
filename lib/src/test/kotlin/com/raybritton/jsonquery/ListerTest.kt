package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.utils.describe
import com.raybritton.jsonquery.utils.list
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ListerTest {

    @Test
    fun testSimple() {
        //Given sample json
        val json1 = """[1,2,3,4]"""
        val json2 = """["a","b","c","d","e"]"""

        //When processed
        val gson = Gson()
        val query = Query(Query.Method.LIST, "")
        val result1 = gson.fromJson<Any>(json1, Any::class.java).list(query)
        val result2 = gson.fromJson<Any>(json2, Any::class.java).list(query)

        //Then check description is accurate
        Assert.assertEquals("json1", "[1.0, 2.0, 3.0, 4.0]", result1)
        Assert.assertEquals("json2", "[a, b, c, d, e]", result2)
    }
}