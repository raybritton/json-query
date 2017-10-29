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
}