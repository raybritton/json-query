package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JsonFileTest3 {

    val json = """
        [{
          "answerId": 63,
          "questionId": 1,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T13:54:33.000Z",
          "updatedAt": "2017-09-11T09:53:32.000Z"
        }, {
          "answerId": 74,
          "questionId": 2,
          "questionRevision": 1,
          "remark": null,
          "answer": 0,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:18:58.000Z",
          "updatedAt": "2017-09-11T10:05:17.000Z"
        }, {
          "answerId": 75,
          "questionId": 3,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T09:55:58.000Z"
        }, {
          "answerId": 76,
          "questionId": 4,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T09:59:52.000Z"
        }, {
          "answerId": 77,
          "questionId": 5,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T10:00:10.000Z"
        }, {
          "answerId": 78,
          "questionId": 6,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T10:00:22.000Z"
        }, {
          "answerId": 79,
          "questionId": 7,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T10:06:51.000Z"
        }, {
          "answerId": 80,
          "questionId": 8,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T09:59:06.000Z"
        }, {
          "answerId": 81,
          "questionId": 9,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T09:59:09.000Z"
        }, {
          "answerId": 82,
          "questionId": 10,
          "questionRevision": 1,
          "remark": null,
          "answer": -1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T09:59:14.000Z"
        }, {
          "answerId": 83,
          "questionId": 11,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-11T10:01:51.000Z"
        }, {
          "answerId": 84,
          "questionId": 12,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 85,
          "questionId": 13,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 86,
          "questionId": 14,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:06.000Z"
        }, {
          "answerId": 87,
          "questionId": 15,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:08.000Z"
        }, {
          "answerId": 88,
          "questionId": 16,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:09.000Z"
        }, {
          "answerId": 89,
          "questionId": 17,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:10.000Z"
        }, {
          "answerId": 90,
          "questionId": 18,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:12.000Z"
        }, {
          "answerId": 91,
          "questionId": 19,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:13.000Z"
        }, {
          "answerId": 92,
          "questionId": 20,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:14.000Z"
        }, {
          "answerId": 93,
          "questionId": 21,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:15.000Z"
        }, {
          "answerId": 94,
          "questionId": 22,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:16.000Z"
        }, {
          "answerId": 95,
          "questionId": 23,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-08T10:41:17.000Z"
        }, {
          "answerId": 96,
          "questionId": 24,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 97,
          "questionId": 25,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 98,
          "questionId": 26,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 99,
          "questionId": 27,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 100,
          "questionId": 28,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 101,
          "questionId": 29,
          "questionRevision": 1,
          "remark": "699",
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-09T08:30:02.000Z"
        }, {
          "answerId": 102,
          "questionId": 30,
          "questionRevision": 2,
          "remark": "89",
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 103,
          "questionId": 31,
          "questionRevision": 1,
          "remark": "555",
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-09T08:30:56.000Z"
        }, {
          "answerId": 104,
          "questionId": 32,
          "questionRevision": 1,
          "remark": "99",
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 105,
          "questionId": 33,
          "questionRevision": 1,
          "remark": "9",
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-09T08:30:31.000Z"
        }, {
          "answerId": 106,
          "questionId": 34,
          "questionRevision": 2,
          "remark": "6",
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:30:15.000Z"
        }, {
          "answerId": 107,
          "questionId": 35,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 108,
          "questionId": 36,
          "questionRevision": 1,
          "remark": "T 666667",
          "answer": 0,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:57:08.000Z"
        }, {
          "answerId": 109,
          "questionId": 161,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-09T07:48:06.000Z"
        }, {
          "answerId": 110,
          "questionId": 162,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 111,
          "questionId": 163,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 112,
          "questionId": 164,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 113,
          "questionId": 165,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 114,
          "questionId": 166,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 115,
          "questionId": 167,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 116,
          "questionId": 168,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 117,
          "questionId": 169,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-09-09T08:25:29.000Z"
        }, {
          "answerId": 118,
          "questionId": 170,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 119,
          "questionId": 171,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:27:43.000Z",
          "updatedAt": "2017-08-21T14:27:43.000Z"
        }, {
          "answerId": 120,
          "questionId": 172,
          "questionRevision": 1,
          "remark": null,
          "answer": 0,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:54:53.000Z",
          "updatedAt": "2017-08-21T14:54:53.000Z"
        }, {
          "answerId": 121,
          "questionId": 173,
          "questionRevision": 1,
          "remark": null,
          "answer": 0,
          "createdByUserId": 4,
          "createdAt": "2017-08-21T14:58:07.000Z",
          "updatedAt": "2017-08-21T15:01:12.000Z"
        }, {
          "answerId": 129,
          "questionId": 12,
          "questionRevision": 2,
          "remark": null,
          "answer": 0,
          "createdByUserId": 4,
          "createdAt": "2017-08-22T10:28:04.000Z",
          "updatedAt": "2017-09-11T10:02:36.000Z"
        }, {
          "answerId": 130,
          "questionId": 13,
          "questionRevision": 2,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-08-22T10:36:44.000Z",
          "updatedAt": "2017-09-11T10:02:45.000Z"
        }, {
          "answerId": 131,
          "questionId": 34,
          "questionRevision": 3,
          "remark": "546585",
          "answer": -1,
          "createdByUserId": 4,
          "createdAt": "2017-08-22T10:36:44.000Z",
          "updatedAt": "2017-09-09T08:31:37.000Z"
        }, {
          "answerId": 150,
          "questionId": 174,
          "questionRevision": 1,
          "remark": null,
          "answer": 1,
          "createdByUserId": 4,
          "createdAt": "2017-09-08T10:39:47.000Z",
          "updatedAt": "2017-09-08T10:39:47.000Z"
        }]
    """.trimIndent()

    @Test
    fun testSelectingAsJson() {
        //Given query to select anything as json
        val query1 = """SELECT "updatedAt" FROM "." WHERE "questionId" == 6 ORDER BY "updatedAt" AS JSON"""
        val query2 = """SELECT "updatedAt" FROM "." WHERE "questionId" == 6 ORDER BY "updatedAt""""
        val query3 = """SELECT "updatedAt" FROM "." WHERE "questionId" == 6 ORDER BY "updatedAt" WITH KEYS"""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result1 = jsonQuery.query(query1)
        val result2 = jsonQuery.query(query2)
        val result3 = jsonQuery.query(query3)

        //Then check only updatedAts where returned
        assertEquals("result1", """[{"updatedAt":"2017-09-11T10:00:22.000Z"}]""", result1)
        assertEquals("result2", """2017-09-11T10:00:22.000Z""", result2)
        assertEquals("result3", """updatedAt: 2017-09-11T10:00:22.000Z""", result3)
    }

    @Test
    fun testSelecting() {
        //Given query to select anything as json
        val query = """SELECT "updatedAt" FROM "." WHERE "questionId" == 6 ORDER BY "updatedAt""""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result = jsonQuery.query(query)

        //Then check only updatedAts where returned
        assertEquals("result", """2017-09-11T10:00:22.000Z""", result)
    }

    @Test
    fun testOrdering() {
        //Given query to select anything as json
        val query1 = """SELECT "updatedAt" FROM "." ORDER BY "updatedAt" LIMIT 3"""
        val query2 = """SELECT "updatedAt" FROM "." ORDER BY "updatedAt" DESC LIMIT 3"""

        //When processed
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)
        val result1 = jsonQuery.query(query1)
        val result2 = jsonQuery.query(query2)

        //Then check only updatedAts where returned
        assertEquals("result1", """[2017-09-11T09:53:32.000Z, 2017-09-11T09:55:58.000Z, 2017-09-11T10:05:17.000Z]""", result1)
        assertEquals("result2", """[2017-09-11T10:05:17.000Z, 2017-09-11T09:55:58.000Z, 2017-09-11T09:53:32.000Z]""", result2)
    }
}