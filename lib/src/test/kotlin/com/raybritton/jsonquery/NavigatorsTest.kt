package com.raybritton.jsonquery

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.tools.getFirstSegment
import com.raybritton.jsonquery.tools.navigate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NavigatorsTest {

    @Test
    fun testFirstSegment() {
        //Given range of nav paths
        val path1 = ".items"
        val path2 = ".items.id"
        val path3 = ".items[id, title]"
        val path4 = ".outer.inner.key"

        //When first segments are retrieved
        val result1 = path1.getFirstSegment()
        val result2 = path2.getFirstSegment()
        val result3 = path3.getFirstSegment()
        val result4 = path4.getFirstSegment()

        //Then check they're correct
        assertEquals("path 1", "items", result1)
        assertEquals("path 2", "items", result2)
        assertEquals("path 3", "items[id, title]", result3)
        assertEquals("path 4", "outer", result4)
    }

    @Test
    fun testFirstSegmentComplexNames() {
        //Given range of nav paths
        val path1 = ".items cont"
        val path2 = ".items\\.34.id"
        val path3 = ".items"
        val path4 = ".outer.inner.key"

        //When first segments are retrieved
        val result1 = path1.getFirstSegment()
        val result2 = path2.getFirstSegment()
        val result3 = path3.getFirstSegment()
        val result4 = path4.getFirstSegment()

        //Then check they're correct
        assertEquals("path 1", "items cont", result1)
        assertEquals("path 2", "items\\.34", result2)
        assertEquals("path 3", "items", result3)
        assertEquals("path 4", "outer", result4)
    }

    @Test
    fun testSimple() {
        //Given simple example
        val map = LinkedTreeMap<String, Any>()
        map.put("inner", LinkedTreeMap<String, Any>())
        (map["inner"] as LinkedTreeMap<String, Any>).put("iKey", "iValue")

        //When navigated to inner object
        val innerObj = map.navigate(".inner")

        //Then check the correct object remains
        assertEquals("type", innerObj!!.javaClass.typeName, "com.google.gson.internal.LinkedTreeMap")
        assertEquals("innerObj size", 1, (innerObj as LinkedTreeMap<String, Any>).size)
        assertEquals("innerObj key and value", "iValue", innerObj.get("iKey"))
    }

    @Test
    fun testLayered() {
        //Given simple example
        val gson = Gson()
        val json1 = """{"inner1":{"ikey1":"ival1","ikey2":"ival2"},"inner2":{"inner2.1":{"iikey1":"iival1"}}}"""
        val map1 = gson.fromJson<Any>(json1, Any::class.java)

        //When navigated to inner object
        val innerObj = map1.navigate(".inner2.inner2\\.1")

        //Then check the correct object remains
        assertEquals("type", innerObj!!.javaClass.typeName, "com.google.gson.internal.LinkedTreeMap")
        assertEquals("innerObj size", 1, (innerObj as LinkedTreeMap<String, Any>).size)
        assertEquals("innerObj key and value", "iival1", innerObj.get("iikey1"))
    }

    @Test
    fun testArrayAccess() {
        //Given json with objects within an arrray
        val gson = Gson()
        val json1 = """[{"k1":"v1"},{"k2":"v2"},{"k3":"v3"}]"""
        val json2 = """{"list":[1,2,3], "list[2]":true}"""
        val map1 = gson.fromJson<Any>(json1, Any::class.java)
        val map2 = gson.fromJson<Any>(json2, Any::class.java)

        //When navigated to second object
        val resultParent1 = map1.navigate(".[1]")
        val resultChild1 = map1.navigate(".[1].k2")
        val resultChild2 = map2.navigate(".list[2]")
        val resultChild3 = map2.navigate(".list\\[2]")

        //Then check the correct values
        assertEquals("second obj", "", "")
        assertEquals("result 1 size", 1, (resultParent1 as LinkedTreeMap<String, Any>).size)
        assertEquals("result 1 key and value", "v2", resultParent1.get("k2"))
        assertEquals("result 1 child", "v2", resultChild1)
        assertEquals("result 2", 3.0, resultChild2)
        assertEquals("result 3", true, resultChild3)
    }
}