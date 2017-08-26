package com.raybritton.jsonquery

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.utils.describe
import com.raybritton.jsonquery.utils.getFirstSegment
import com.raybritton.jsonquery.utils.navigate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertEquals("type",innerObj.javaClass.typeName,"com.google.gson.internal.LinkedTreeMap")
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
        assertEquals("type", innerObj.javaClass.typeName,"com.google.gson.internal.LinkedTreeMap")
        assertEquals("innerObj size", 1, (innerObj as LinkedTreeMap<String, Any>).size)
        assertEquals("innerObj key and value", "iival1", innerObj.get("iikey1"))
    }
}