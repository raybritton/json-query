package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test

class JsonFileTest2 {
    val json = """
        {
          "accounting": [
            {
              "firstName": "John",
              "lastName": "Doe",
              "age": 23
            },
            {
              "firstName": "Mary",
              "lastName": "Smith",
              "age": 32
            }
          ],
          "sales": [
            {
              "firstName": "Sally",
              "lastName": "Green",
              "age": 27
            },
            {
              "firstName": "Jim",
              "lastName": "Galley",
              "age": 41
            }
          ]
        }
    """.trimIndent()

    @Test
    fun testSelecting() {
        //Given the json above and two queries to get people
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val query1 = """SELECT ("firstName", "lastName") FROM "accounting" WHERE "age" > 23"""
        val query2 = """SELECT ("firstName", "lastName") FROM "sales" WHERE "age" > 23"""

        //When processed
        val result1 = jsonQuery.query(query1)
        val result2 = jsonQuery.query(query2)

        //Then check [] are only shown for multiple results
        assertEquals("result 1", "{Mary, Smith}", result1)
        assertEquals("result 2", "[{Sally, Green}, {Jim, Galley}]", result2)
    }
}