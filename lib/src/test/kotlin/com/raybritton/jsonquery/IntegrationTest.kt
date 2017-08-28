package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IntegrationTest {

    private val json = """
        {
          "title": "List of People",
          "count": 10,
          "page": 1,
          "data": [
            {
              "id": 0,
              "name": "Person A",
              "age": 20,
              "metadata": {
                "data1": "value1",
                "data2": "value2"
              }
            },
            {
              "id": 1,
              "name": "Person B",
              "age": 20,
              "metadata": {
                "data1": "value1",
                "data2": "value2"
              }
            },
            {
              "id": 2,
              "name": "Person C",
              "age": 20,
              "metadata": {
                "data1": "value1"
              }
            },
            {
              "id": 3,
              "name": "Person D",
              "age": 20,
              "metadata": {
                "data1": "value1",
                "data2": "value2"
              }
            },
            {
              "id": 4,
              "name": "Person E",
              "age": 20,
              "metadata": {
                "data1": "value1"
              }
            }
          ]
        }
    """

    @Test
    fun testMultipleDescribes() {
        //Given json above
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)

        //When several describe queries are run
        val output1 = jsonQuery.query("DESCRIBE \".\"")
        val output2 = jsonQuery.query("DESCRIBE \".title\"")
        val output3 = jsonQuery.query("DESCRIBE \".data\"")
        val output4 = jsonQuery.query("DESCRIBE \".data\" WHERE \"id\" > 2")
        val output5 = jsonQuery.query("DESCRIBE \".data\" LIMIT 1")
        val output6 = jsonQuery.query("DESCRIBE \".data\" SKIP 1")
        val output7 = jsonQuery.query("DESCRIBE \".data\" WHERE \"name\" == \"Person E\" ")

        //Then check results
        assertEquals("output 1", "OBJECT(STRING, NUMBER, NUMBER, ARRAY(OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING, STRING))[3], OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING))[2]))", output1)
        assertEquals("output 2", "STRING", output2)
        assertEquals("output 3", "ARRAY(OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING, STRING))[3], OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING))[2])", output3)
        assertEquals("output 4", "ARRAY(OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING, STRING)), OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING)))", output4)
        assertEquals("output 5", "ARRAY(OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING, STRING)))", output5)
        assertEquals("output 6", "ARRAY(OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING, STRING))[2], OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING))[2])", output6)
        assertEquals("output 7", "ARRAY(OBJECT(NUMBER, STRING, NUMBER, OBJECT(STRING)))", output7)
    }

    @Test
    fun testSimple() {
        //Given sample json
        val json1 = """{"key1":"value1"}"""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json1)
        val result1 = jsonQuery.query("DESCRIBE \".\"")

        //Then check description is accurate
        assertEquals("json1", "OBJECT(STRING)", result1)
    }

    @Test
    fun testMultipleListers() {
        //Given json above
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)

        //When several describe queries are run
        val output1 = jsonQuery.query("SELECT \".data\"")
        val output2 = jsonQuery.query("SELECT (\"id\") IN \".data\"")
        val output3 = jsonQuery.query("SELECT (\"id\") IN \".data\" WHERE \"name\" # \"B\"")
        val output4 = jsonQuery.query("SELECT (\"id\", \"name\") IN \".data\" WHERE \"name\" # \"B\"")

        //Then check results
        assertEquals("output 1", "[{0.0, Person A, 20.0, {value1, value2}}, {1.0, Person B, 20.0, {value1, value2}}, {2.0, Person C, 20.0, {value1}}, {3.0, Person D, 20.0, {value1, value2}}, {4.0, Person E, 20.0, {value1}}]", output1)
        assertEquals("output 2", "[0.0, 1.0, 2.0, 3.0, 4.0]", output2)
        assertEquals("output 3", "[1.0]", output3)
        assertEquals("output 4", "[{1.0, Person B}]", output4)
    }
}