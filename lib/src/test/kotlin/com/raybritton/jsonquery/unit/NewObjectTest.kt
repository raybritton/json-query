package com.raybritton.jsonquery.unit

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.tools.filter
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NewObjectTest {
    private val gson = Gson()

    @Test
    fun filterObjectNoChanges() {
        val jsonObject = gson.fromJson<Any>("{}", Any::class.java)
        val newObject = jsonObject.filter(Query(Query.Method.SELECT, "."))
        assertTrue("no changes", newObject == jsonObject)
        assertTrue("exact same", newObject === jsonObject)
    }

    @Test
    fun filterObjectWithChanges() {
        val jsonObject = gson.fromJson<Any>("{'list':{}}", Any::class.java)
        val newObject = jsonObject.filter(Query(Query.Method.SELECT, ".", Query.TargetExtra.KEYS))
        assertTrue("has changes", newObject != jsonObject)
        assertTrue("new obj", newObject !== jsonObject)
    }

    @Test
    fun filterArrayNoChanges() {
        val jsonObject = gson.fromJson<Any>("[]", Any::class.java)
        val newObject = jsonObject.filter(Query(Query.Method.SELECT, "."))
        assertTrue("no changes", newObject == jsonObject)
        assertTrue("exact same", newObject === jsonObject)
    }

    @Test
    fun filterArrayWithChanges() {
        val jsonObject = gson.fromJson<Any>("[1,2]", Any::class.java)
        val newObject = jsonObject.filter(Query(Query.Method.SELECT, ".", Query.TargetExtra.VALUES))
        assertTrue("has changes", newObject != jsonObject)
        assertTrue("new obj", newObject !== jsonObject)
    }
}