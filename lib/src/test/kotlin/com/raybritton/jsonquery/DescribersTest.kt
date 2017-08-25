package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.utils.describe
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
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe()
        val result2 = gson.fromJson<Any>(json2, Any::class.java).describe()
        val result3 = gson.fromJson<Any>(json3, Any::class.java).describe()
        val result4 = gson.fromJson<Any>(json4, Any::class.java).describe()
        val result5 = gson.fromJson<Any>(json5, Any::class.java).describe()

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING)", result1)
        assertEquals("json2", "OBJECT(STRING, STRING)", result2)
        assertEquals("json3", "OBJECT(BOOLEAN, NUMBER, NUMBER)", result3)
        assertEquals("json4", "ARRAY(STRING[3])", result4)
        assertEquals("json5", "ARRAY(STRING, BOOLEAN, NUMBER)", result5)
    }

    @Test
    fun testObjectInObject() {
        //Given sample json
        val json1 = """{"key1":"value1", "key2":{"iKey1":true}}"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe()

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING, OBJECT(BOOLEAN))", result1)
    }

    @Test
    fun testListInList() {
        //Given sample json
        val json1 = """["a",[1,2,3]]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe()

        //Then check description is accurate
        assertEquals("json1", "ARRAY(STRING, ARRAY(NUMBER[3]))", result1)
    }

    @Test
    fun testComplex() {
        //Given sample json
        val json1 = """[{"a":"value","b":9,"a1":[1,2,3],"a2":[{"a":"a"},{"b":"b"},{"c":"c","cn":5},true]}]"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe()

        //Then check description is accurate
        assertEquals("json1", "ARRAY(OBJECT(STRING, NUMBER, ARRAY(NUMBER[3]), ARRAY(OBJECT(STRING)[2], OBJECT(STRING, NUMBER), BOOLEAN)))", result1)
    }

    @Test
    fun testSamples() {
        //Given sample json
        val json1 = """{"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}"""
        val json2 = """{"widget":{"debug":"on","window":{"title":"Sample Konfabulator Widget","name":"main_window","width":500,"height":500},"image":{"src":"Images/Sun.png","name":"sun1","hOffset":250,"vOffset":250,"alignment":"center"},"text":{"data":"Click Here","size":36,"style":"bold","name":"text1","hOffset":250,"vOffset":100,"alignment":"center","onMouseUp":"sun1.opacity = (sun1.opacity / 100) * 90;"}}}"""
        val json3 = """{"menu":{"header":"SVG Viewer","items":[{"id":"Open"},{"id":"OpenNew","label":"Open New"},null,{"id":"ZoomIn","label":"Zoom In"},{"id":"ZoomOut","label":"Zoom Out"},{"id":"OriginalView","label":"Original View"},null,{"id":"Quality"},{"id":"Pause"},{"id":"Mute"},null,{"id":"Find","label":"Find..."},{"id":"FindAgain","label":"Find Again"},{"id":"Copy"},{"id":"CopyAgain","label":"Copy Again"},{"id":"CopySVG","label":"Copy SVG"},{"id":"ViewSVG","label":"View SVG"},{"id":"ViewSource","label":"View Source"},{"id":"SaveAs","label":"Save As"},null,{"id":"Help"},{"id":"About","label":"About Adobe CVG Viewer..."}]}}"""

        //When processed
        val gson = Gson()
        val result1 = gson.fromJson<Any>(json1, Any::class.java).describe()
        val result2 = gson.fromJson<Any>(json2, Any::class.java).describe()
        val result3 = gson.fromJson<Any>(json3, Any::class.java).describe()

        //Then check description is accurate
        assertEquals("json1", "OBJECT(OBJECT(STRING, OBJECT(STRING, OBJECT(OBJECT(STRING, STRING, STRING, STRING, STRING, OBJECT(STRING, ARRAY(STRING[2])), STRING)))))", result1)
        assertEquals("json2", "OBJECT(OBJECT(STRING, OBJECT(STRING, STRING, NUMBER, NUMBER), OBJECT(STRING, STRING, NUMBER, NUMBER, STRING), OBJECT(STRING, NUMBER, STRING, STRING, NUMBER, NUMBER, STRING, STRING)))", result2)
        assertEquals("json3", "OBJECT(OBJECT(STRING, ARRAY(OBJECT(STRING)[6], OBJECT(STRING, STRING)[12], NULL[4])))", result3)
    }
}