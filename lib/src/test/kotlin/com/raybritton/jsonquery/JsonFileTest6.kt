package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JsonFileTest6 {

    val json = """[{"id":"343abed-243dd-adecb-f344edf4a","name":"Example1","data":{"bits":[1,2,3,4,5],"code":"json"}},{"id":"343abed-243dd-adecb-f344edf4b","name":"Example2","data":{"bits":[2,2,3,4,5],"code":"query"}},{"id":"343abed-243dd-adecb-f344edf4c","name":"Example3","data":{"bits":[1,3,3,4,5],"code":"language"}},{"id":"343abed-243dd-adecb-f344edf4d","name":"Example4","data":{"bits":[1,2,4,4,5],"code":"blank"}},{"id":"343abed-243dd-adecb-f344edf4e","name":"Example5","data":{"bits":[5,2,3,4,5],"code":"graphical"}},{"id":"343abed-243dd-adecb-f344edf4f","name":"Example6","data":{"bits":[5,5,3,4,5],"code":"user"}},{"id":"343abed-243dd-adecb-f344edf50","name":"Example7","data":{"bits":[1,1,1,4,5],"code":"interface"}},{"id":"343abed-243dd-adecb-f344edf51","name":"Example8","data":{"bits":[1,2,4,4,4],"code":"eod"}}]"""

    @Test
    fun testSelectingElement() {
        //Given query to select element from object in the list as json
        val query1 = """SELECT "." WHERE ".data.code" # "s" ORDER BY ".id" AS JSON"""
        val query2 = """SELECT "." WHERE ".data.code" # "s" ORDER BY ".id" DESC AS JSON"""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result1 = jsonQuery.query(query1)
        val result2 = jsonQuery.query(query2)

        //Then only the globalids are returned (and not all elements)
        assertEquals("result1", """[{"id":"343abed-243dd-adecb-f344edf4a","name":"Example1","data":{"bits":[1.0,2.0,3.0,4.0,5.0],"code":"json"}},{"id":"343abed-243dd-adecb-f344edf4f","name":"Example6","data":{"bits":[5.0,5.0,3.0,4.0,5.0],"code":"user"}}]""", result1)
        assertEquals("result2", """[{"id":"343abed-243dd-adecb-f344edf4f","name":"Example6","data":{"bits":[5.0,5.0,3.0,4.0,5.0],"code":"user"}},{"id":"343abed-243dd-adecb-f344edf4a","name":"Example1","data":{"bits":[1.0,2.0,3.0,4.0,5.0],"code":"json"}}]""", result2)
    }

    val json2 = """{"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}"""

    @Test
    fun testSimpleSelect() {
        //Given query to select element from object in the list as json
        val query1 = """SELECT "glossary" FROM "." AS JSON"""
        val query2 = """SELECT "glossary" FROM "." WITH KEYS"""
        val query3 = """SELECT "glossary" FROM ".""""
        val query4 = """DESCRIBE "glossary" FROM ".""""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json2)
        val result1 = jsonQuery.query(query1)
        val result2 = jsonQuery.query(query2)
        val result3 = jsonQuery.query(query3)
        val result4 = jsonQuery.query(query4)

        //Then only the globalids are returned (and not all elements)
        assertEquals("result1", """{"glossary":{"title":"example glossary","GlossDiv":{"title":"S","GlossList":{"GlossEntry":{"ID":"SGML","SortAs":"SGML","GlossTerm":"Standard Generalized Markup Language","Acronym":"SGML","Abbrev":"ISO 8879:1986","GlossDef":{"para":"A meta-markup language, used to create markup languages such as DocBook.","GlossSeeAlso":["GML","XML"]},"GlossSee":"markup"}}}}}""", result1)
        assertEquals("result2", """{glossary: {title: example glossary, GlossDiv: {title: S, GlossList: {GlossEntry: {ID: SGML, SortAs: SGML, GlossTerm: Standard Generalized Markup Language, Acronym: SGML, Abbrev: ISO 8879:1986, GlossDef: {para: A meta-markup language, used to create markup languages such as DocBook., GlossSeeAlso: [GML, XML]}, GlossSee: markup}}}}}""", result2)
        assertEquals("result3", """{example glossary, {S, {{SGML, SGML, Standard Generalized Markup Language, SGML, ISO 8879:1986, {A meta-markup language, used to create markup languages such as DocBook., [GML, XML]}, markup}}}}""", result3)
        assertEquals("result4", """OBJECT(OBJECT(STRING, OBJECT(STRING, OBJECT(OBJECT(STRING[6], OBJECT(STRING, ARRAY(STRING[2])))))))""", result4)
    }
}