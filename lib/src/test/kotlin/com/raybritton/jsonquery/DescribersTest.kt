package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.printer.describe
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DescribersTest {

    @Test
    fun testSimple() {
        //Given sample json
        val json1 = """{"key1":"value1"}"""
        val json2 = """{"key1":"value1","key2":"value2"}"""
        val json3 = """{"key1":true,"key2":3,"key3":0.1}"""
        val json4 = """["a", "b", "c"]"""
        val json5 = """["a", true, 3]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))
        val result2 = gson.fromJson<Any>(json2, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))
        val result3 = gson.fromJson<Any>(json3, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))
        val result4 = gson.fromJson<Any>(json4, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))
        val result5 = gson.fromJson<Any>(json5, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING)", result1)
        assertEquals("json2", "OBJECT(STRING[2])", result2)
        assertEquals("json3", "OBJECT(BOOLEAN, NUMBER[2])", result3)
        assertEquals("json4", "ARRAY(STRING[3])", result4)
        assertEquals("json5", "ARRAY(STRING, BOOLEAN, NUMBER)", result5)
    }

    @Test
    fun testSimplePretty() {
        //Given sample json
        val json1 = """{"key1":"value1"}"""
        val json2 = """{"key1":"value1","key2":"value2"}"""
        val json3 = """{"key1":true,"key2":3,"key3":0.1}"""
        val json4 = """["a", "b", "c"]"""
        val json5 = """["a", true, 3]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))
        val result2 = gson.fromJson<Any>(json2, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))
        val result3 = gson.fromJson<Any>(json3, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))
        val result4 = gson.fromJson<Any>(json4, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))
        val result5 = gson.fromJson<Any>(json5, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING)", result1)
        assertEquals("json2", "OBJECT(STRING[2])", result2)
        assertEquals("json3", "OBJECT(\n  BOOLEAN, \n  NUMBER[2])", result3)
        assertEquals("json4", "ARRAY(STRING[3])", result4)
        assertEquals("json5", "ARRAY(\n  STRING, \n  BOOLEAN, \n  NUMBER)", result5)
    }

    @Test
    fun testObjectInObject() {
        //Given sample json
        val json1 = """{"key1":"value1", "key2":{"iKey1":true}}"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING, OBJECT(BOOLEAN))", result1)
    }

    @Test
    fun testObjectInObjectPretty() {
        //Given sample json
        val json1 = """{"key1":"value1", "key2":{"iKey1":true}}"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))

        //Then check description is accurate
        assertEquals("json1", "OBJECT(\n  STRING, \n  OBJECT(BOOLEAN))", result1)
    }

    @Test
    fun testListInList() {
        //Given sample json
        val json1 = """["a",[1,2,3]]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))

        //Then check description is accurate
        assertEquals("json1", "ARRAY(STRING, ARRAY(NUMBER[3]))", result1)
    }

    @Test
    fun testListInListPretty() {
        //Given sample json
        val json1 = """["a",[1,2,3]]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))

        //Then check description is accurate
        assertEquals("json1", "ARRAY(\n  STRING, \n  ARRAY(NUMBER[3]))", result1)
    }

    @Test
    fun testComplex() {
        //Given sample json
        val json1 = """[{"a":"value","b":9,"a1":[1,2,3],"a2":[{"a":"a"},{"b":"b"},{"c":"c","cn":5},true]}]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))

        //Then check description is accurate
        assertEquals("json1", "ARRAY(OBJECT(STRING, NUMBER, ARRAY(NUMBER[3]), ARRAY(OBJECT(STRING)[2], OBJECT(STRING, NUMBER), BOOLEAN)))", result1)
    }

    @Test
    fun testComplexPretty() {
        //Given sample json
        val json1 = """[{"a":"value","b":9,"a1":[1,2,3],"a2":[{"a":"a"},{"b":"b"},{"c":"c","cn":5},true]}]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))

        //Then check description is accurate
        assertEquals("json1", "ARRAY(\n  OBJECT(\n    STRING, \n    NUMBER, \n    ARRAY(NUMBER[3]), \n    ARRAY(\n      OBJECT(STRING)[2], \n      OBJECT(\n        STRING, \n        NUMBER), \n      BOOLEAN)))", result1)
    }

    @Test
    fun testSamples() {
        //Given sample json
        val json1 = """{"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}"""
        val json2 = """{"widget":{"debug":"on","window":{"title":"Sample Konfabulator Widget","name":"main_window","width":500,"height":500},"image":{"src":"Images/Sun.png","name":"sun1","hOffset":250,"vOffset":250,"alignment":"center"},"text":{"data":"Click Here","size":36,"style":"bold","name":"text1","hOffset":250,"vOffset":100,"alignment":"center","onMouseUp":"sun1.opacity = (sun1.opacity / 100) * 90;"}}}"""
        val json3 = """{"menu":{"header":"SVG Viewer","items":[{"id":"Open"},{"id":"OpenNew","label":"Open New"},null,{"id":"ZoomIn","label":"Zoom In"},{"id":"ZoomOut","label":"Zoom Out"},{"id":"OriginalView","label":"Original View"},null,{"id":"Quality"},{"id":"Pause"},{"id":"Mute"},null,{"id":"Find","label":"Find..."},{"id":"FindAgain","label":"Find Again"},{"id":"Copy"},{"id":"CopyAgain","label":"Copy Again"},{"id":"CopySVG","label":"Copy SVG"},{"id":"ViewSVG","label":"View SVG"},{"id":"ViewSource","label":"View Source"},{"id":"SaveAs","label":"Save As"},null,{"id":"Help"},{"id":"About","label":"About Adobe CVG Viewer..."}]}}"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))
        val result2 = gson.fromJson<Any>(json2, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))
        val result3 = gson.fromJson<Any>(json3, Any::class.java).describe(Query(Query.Method.DESCRIBE, "."))

        //Then check description is accurate
        assertEquals("json1", "OBJECT(OBJECT(STRING, OBJECT(STRING, OBJECT(OBJECT(STRING[6], OBJECT(STRING, ARRAY(STRING[2])))))))", result1)
        assertEquals("json2", "OBJECT(OBJECT(STRING, OBJECT(STRING[2], NUMBER[2]), OBJECT(STRING[3], NUMBER[2]), OBJECT(STRING[5], NUMBER[3])))", result2)
        assertEquals("json3", "OBJECT(OBJECT(STRING, ARRAY(OBJECT(STRING)[6], OBJECT(STRING[2])[12], NULL[4])))", result3)
    }

    @Test
    fun testSamplesPretty() {
        //Given sample json
        val json1 = """{"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}"""
        val json2 = """{"widget":{"debug":"on","window":{"title":"Sample Konfabulator Widget","name":"main_window","width":500,"height":500},"image":{"src":"Images/Sun.png","name":"sun1","hOffset":250,"vOffset":250,"alignment":"center"},"text":{"data":"Click Here","size":36,"style":"bold","name":"text1","hOffset":250,"vOffset":100,"alignment":"center","onMouseUp":"sun1.opacity = (sun1.opacity / 100) * 90;"}}}"""
        val json3 = """{"menu":{"header":"SVG Viewer","items":[{"id":"Open"},{"id":"OpenNew","label":"Open New"},null,{"id":"ZoomIn","label":"Zoom In"},{"id":"ZoomOut","label":"Zoom Out"},{"id":"OriginalView","label":"Original View"},null,{"id":"Quality"},{"id":"Pause"},{"id":"Mute"},null,{"id":"Find","label":"Find..."},{"id":"FindAgain","label":"Find Again"},{"id":"Copy"},{"id":"CopyAgain","label":"Copy Again"},{"id":"CopySVG","label":"Copy SVG"},{"id":"ViewSVG","label":"View SVG"},{"id":"ViewSource","label":"View Source"},{"id":"SaveAs","label":"Save As"},null,{"id":"Help"},{"id":"About","label":"About Adobe CVG Viewer..."}]}}"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))
        val result2 = gson.fromJson<Any>(json2, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))
        val result3 = gson.fromJson<Any>(json3, Any::class.java).describe(Query(Query.Method.DESCRIBE, ".", pretty = true))

        //Then check description is accurate
        assertEquals("json1", "OBJECT(\n  OBJECT(\n    STRING, \n    OBJECT(\n      STRING, \n      OBJECT(\n        OBJECT(\n          STRING[6], \n          OBJECT(\n            STRING, \n            ARRAY(STRING[2])))))))", result1)
        assertEquals("json2", "OBJECT(\n  OBJECT(\n    STRING, \n    OBJECT(\n      STRING[2], \n      NUMBER[2]), \n    OBJECT(\n      STRING[3], \n      NUMBER[2]), \n    OBJECT(\n      STRING[5], \n      NUMBER[3])))", result2)
        assertEquals("json3", "OBJECT(\n  OBJECT(\n    STRING, \n    ARRAY(\n      OBJECT(STRING)[6], \n      OBJECT(STRING[2])[12], \n      NULL[4])))", result3)
    }
}