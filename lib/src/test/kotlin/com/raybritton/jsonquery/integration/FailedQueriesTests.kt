package com.raybritton.jsonquery.integration

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * These are tests checking fixes for queries that failed in qa
 */
class FailedQueriesTests {
    @Test
    fun `selecting distinct keys from nested query`() {
        val json = """[
              {
                "name": "First",
                "date": "2019-05-01"
              },
              {
                "name": "Second",
                "date": "2019-05-02"
              },
              {
                "name": "Third",
                "date": "2019-05-03"
              },
              {
                "name": "Fourth",
                "date": "2019-05-04"
              },
              {
                "name": "Fifth",
                "date": "2019-05-05"
              },
              {
                "name": "Sixth",
                "date": "2019-05-06"
              }
            ]"""
        val result = JsonQuery(json).query("""SELECT DISTINCT KEYS FROM (SELECT "date" FROM "." WHERE "date" < "2019-05-05" AS JSON)""")

        assertEquals("[date]", result)
    }
}