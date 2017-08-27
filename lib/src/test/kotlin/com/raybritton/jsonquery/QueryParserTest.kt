package com.raybritton.jsonquery

import com.raybritton.jsonquery.models.NullCompare
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.utils.ELEMENT
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QueryParserTest {

    val testJson = """
        {
  "items": [
    {
      "id": 0,
      "title": "null"
    },
    {
      "id": 1,
      "title": "example"
    },
    {
      "id": 2,
      "title": "example with extras"
    },
    {
      "id": 3,
      "title": null
    },
    {
      "id": 4,
      "attr": {
        "history": {
          "pageCount": 6,
          "pages": [
            "grsadfdfsa",
            "dsafasfs",
            "hthtrsdgf",
            "rthaetnfhs",
            "getrgeth",
            "htehtrsfd"
          ]
        }
      }
    },
    {
      "id": 5,
      "title": "something different"
    }
  ],
  "ages": [
    32,
    58,
    37,
    10,
    24,
    67
  ],
  "user": {
    "usageIds": [
      343,
      345,
      832,
      646,
      732
    ]
  }
}
    """

    @Test
    fun testSimple() {
        //Given sample queries
        val queryStmt1 = "DESCRIBE \".\""
        val queryStmt2 = "DESCRIBE      \".items\""
        val queryStmt3 = "LIST VALUES IN \".items.id\""
        val queryStmt4 = "LIST \".items.id\""
        val queryStmt5 = "GET \".items.id\""

        //When processed
        val query1 = queryStmt1.toQuery()
        val query2 = queryStmt2.toQuery()
        val query3 = queryStmt3.toQuery()
        val query4 = queryStmt4.toQuery()
        val query5 = queryStmt5.toQuery()

        //Then check it matches
        assertEquals("query1 method", Query.Method.DESCRIBE, query1.method)
        assertEquals("query1 target", ".", query1.target)
        assertEquals("query1 target extras", null, query1.targetExtra)
        assertEquals("query1 target keys size", 0, query1.targetKeys.size)
        assertEquals("query1 isjson", false, query1.asJson)
        assertEquals("query1 withKeys", false, query1.withKeys)
        assertEquals("query1 skip", null, query1.skip)
        assertEquals("query1 limit", null, query1.limit)
        assertEquals("query1 where", null, query1.where)

        assertEquals("query2 method", Query.Method.DESCRIBE, query2.method)
        assertEquals("query2 target", ".items", query2.target)
        assertEquals("query2 target extras", null, query2.targetExtra)
        assertEquals("query2 target keys size", 0, query2.targetKeys.size)
        assertEquals("query2 isjson", false, query2.asJson)
        assertEquals("query2 withKeys", false, query2.withKeys)
        assertEquals("query2 skip", null, query2.skip)
        assertEquals("query2 limit", null, query2.limit)
        assertEquals("query2 where", null, query2.where)

        assertEquals("query3 method", Query.Method.LIST, query3.method)
        assertEquals("query3 target", ".items.id", query3.target)
        assertEquals("query3 target extras", Query.TargetExtra.VALUES, query3.targetExtra)
        assertEquals("query3 target keys size", 0, query3.targetKeys.size)
        assertEquals("query3 isjson", false, query3.asJson)
        assertEquals("query3 withKeys", false, query3.withKeys)
        assertEquals("query3 skip", null, query3.skip)
        assertEquals("query3 limit", null, query3.limit)
        assertEquals("query3 where", null, query3.where)

        assertEquals("query4 method", Query.Method.LIST, query4.method)
        assertEquals("query4 target", ".items.id", query4.target)
        assertEquals("query4 target extras", null, query4.targetExtra)
        assertEquals("query4 target keys size", 0, query4.targetKeys.size)
        assertEquals("query4 isjson", false, query4.asJson)
        assertEquals("query4 withKeys", false, query4.withKeys)
        assertEquals("query4 skip", null, query4.skip)
        assertEquals("query4 limit", null, query4.limit)
        assertEquals("query4 where", null, query4.where)

        assertEquals("query5 method", Query.Method.GET, query5.method)
        assertEquals("query5 target", ".items.id", query5.target)
        assertEquals("query5 target extras", null, query5.targetExtra)
        assertEquals("query5 target keys size", 0, query5.targetKeys.size)
        assertEquals("query5 isjson", false, query5.asJson)
        assertEquals("query5 withKeys", false, query5.withKeys)
        assertEquals("query5 skip", null, query5.skip)
        assertEquals("query5 limit", null, query5.limit)
        assertEquals("query5 where", null, query5.where)
    }

    @Test
    fun testSkipLimit() {
        //Given sample queries
        val skip = "GET \".id\" SKIP 1"
        val limit = "LIST \".id\" LIMIT 9"
        val both = "LIST \".id\" SKIP 3 LIMIT 4"
        val bothRev = "LIST \".id\" LIMIT 2 SKIP 3"

        //When processed
        val resultSkip = skip.toQuery()
        val resultLimit = limit.toQuery()
        val resultBoth = both.toQuery()
        val resultBothRev = bothRev.toQuery()

        //Then check it matches
        assertEquals("skip method", Query.Method.GET, resultSkip.method)
        assertEquals("skip target", ".id", resultSkip.target)
        assertEquals("skip target extras", null, resultSkip.targetExtra)
        assertEquals("skip target keys size", 0, resultSkip.targetKeys.size)
        assertEquals("skip isjson", false, resultSkip.asJson)
        assertEquals("skip withKeys", false, resultSkip.withKeys)
        assertEquals("skip skip", 1, resultSkip.skip)
        assertEquals("skip limit", null, resultSkip.limit)
        assertEquals("skip where", null, resultSkip.where)

        assertEquals("limit method", Query.Method.LIST, resultLimit.method)
        assertEquals("limit target", ".id", resultLimit.target)
        assertEquals("limit target extras", null, resultLimit.targetExtra)
        assertEquals("limit target keys size", 0, resultLimit.targetKeys.size)
        assertEquals("limit isjson", false, resultLimit.asJson)
        assertEquals("limit withKeys", false, resultLimit.withKeys)
        assertEquals("limit skip", null, resultLimit.skip)
        assertEquals("limit limit", 9, resultLimit.limit)
        assertEquals("limit where", null, resultLimit.where)

        assertEquals("both method", Query.Method.LIST, resultBoth.method)
        assertEquals("both target", ".id", resultBoth.target)
        assertEquals("both target extras", null, resultBoth.targetExtra)
        assertEquals("both target keys size", 0, resultBoth.targetKeys.size)
        assertEquals("both isjson", false, resultBoth.asJson)
        assertEquals("both withKeys", false, resultBoth.withKeys)
        assertEquals("both skip", 3, resultBoth.skip)
        assertEquals("both limit", 4, resultBoth.limit)
        assertEquals("both where", null, resultBoth.where)

        assertEquals("bothrev method", Query.Method.LIST, resultBothRev.method)
        assertEquals("bothrev target", ".id", resultBothRev.target)
        assertEquals("bothrev target extras", null, resultBothRev.targetExtra)
        assertEquals("bothrev target keys size", 0, resultBothRev.targetKeys.size)
        assertEquals("bothrev isjson", false, resultBothRev.asJson)
        assertEquals("bothrev withKeys", false, resultBothRev.withKeys)
        assertEquals("bothrev skip", 3, resultBothRev.skip)
        assertEquals("bothrev limit", 2, resultBothRev.limit)
        assertEquals("bothrev where", null, resultBothRev.where)
    }

    @Test
    fun testFullStatement() {
        //Given a full query
        val query = "LIST VALUES IN \".items.id\" WHERE \"title\" == \"Hello\" SKIP 1 LIMIT 10 AS JSON WITH KEYS"

        //When processed
        val result = query.toQuery()

        //Then check it's correct
        assertEquals("method", Query.Method.LIST, result.method)
        assertEquals("target", ".items.id", result.target)
        assertEquals("target extras", Query.TargetExtra.VALUES, result.targetExtra)
        assertEquals("target keys size", 0, result.targetKeys.size)
        assertEquals("where field", "title", result.where!!.field)
        assertEquals("where operator", Query.Where.Operator.EQUAL, result.where.operator)
        assertEquals("where compare", "Hello", result.where.compare)
        assertEquals("skip count", 1, result.skip)
        assertEquals("limit count", 10, result.limit)
        assertEquals("as json", true, result.asJson)
        assertEquals("with keys", true, result.withKeys)
    }

    @Test
    fun testWhereNumber() {
        //Given a range of where queries
        val number_1 = "DESCRIBE \".items\" WHERE \"id\" > 0.0"
        val number_2 = "DESCRIBE \".items\" WHERE \"id\" < 10"
        val number_3 = "DESCRIBE \".items\" WHERE \"id\" != -10"
        val number_4 = "DESCRIBE \".items\" WHERE \"id\" == 1e6"

        //When process
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(testJson)
        val number_1_query = number_1.toQuery()
        val number_2_query = number_2.toQuery()
        val number_3_query = number_3.toQuery()
        val number_4_query = number_4.toQuery()

        val number_1_result = jsonQuery.query(number_1)
        val number_2_result = jsonQuery.query(number_2)
        val number_3_result = jsonQuery.query(number_3)
        val number_4_result = jsonQuery.query(number_4)

        //Then check they're correct
        assertEquals("n1q field", "id", number_1_query.where!!.field)
        assertEquals("n1q operator", Query.Where.Operator.GREATER_THAN, number_1_query.where.operator)
        assertEquals("n1q compare", 0.0, number_1_query.where.compare)

        assertEquals("n2q field", "id", number_2_query.where!!.field)
        assertEquals("n2q operator", Query.Where.Operator.LESS_THAN, number_2_query.where.operator)
        assertEquals("n2q compare", 10.0, number_2_query.where.compare)

        assertEquals("n3q field", "id", number_3_query.where!!.field)
        assertEquals("n3q operator", Query.Where.Operator.NOT_EQUAL, number_3_query.where.operator)
        assertEquals("n3q compare", -10.0, number_3_query.where.compare)

        assertEquals("n4q field", "id", number_4_query.where!!.field)
        assertEquals("n4q operator", Query.Where.Operator.EQUAL, number_4_query.where.operator)
        assertEquals("n4q compare", 1000000.0, number_4_query.where.compare)

        assertEquals("n1 result", "ARRAY(OBJECT(NUMBER, STRING)[2], OBJECT(NUMBER, NULL), OBJECT(NUMBER, OBJECT(ARRAY(STRING[6]))))", number_1_result)
        assertEquals("n2 result", "ARRAY(OBJECT(NUMBER, STRING)[3], OBJECT(NUMBER, NULL), OBJECT(NUMBER, OBJECT(ARRAY(STRING[6]))))", number_2_result)
        assertEquals("n3 result", "ARRAY(OBJECT(NUMBER, STRING)[2], OBJECT(NUMBER, NULL), OBJECT(NUMBER, OBJECT(ARRAY(STRING[6]))))", number_3_result)
        assertEquals("n4 result", "ARRAY()", number_4_result)
    }

    @Test
    fun testWhereString() {
        //Given a range of where queries
        val string_1 = "DESCRIBE \".items\" WHERE \"title\" # \"example\""
        val string_2 = "DESCRIBE \".items\" WHERE \"title\" !# \"example\""

        //When process
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(testJson)
        val string_1_query = string_1.toQuery()
        val string_2_query = string_2.toQuery()

        val string_1_result = jsonQuery.query(string_1)
        val string_2_result = jsonQuery.query(string_2)

        //Then check they're correct
        assertEquals("s1q field", "title", string_1_query.where!!.field)
        assertEquals("s1q operator", Query.Where.Operator.CONTAINS, string_1_query.where.operator)
        assertEquals("s1q compare", "example", string_1_query.where.compare)

        assertEquals("s2q field", "title", string_2_query.where!!.field)
        assertEquals("s2q operator", Query.Where.Operator.NOT_CONTAINS, string_2_query.where.operator)
        assertEquals("s2q compare", "example", string_2_query.where.compare)

        assertEquals("s1 result", "ARRAY(OBJECT(NUMBER, STRING)[2])", string_1_result)
        assertEquals("s2 result", "ARRAY(OBJECT(NUMBER, STRING)[2], OBJECT(NUMBER, NULL), OBJECT(NUMBER, OBJECT(OBJECT(NUMBER, ARRAY(STRING[6])))))", string_2_result)
    }

    @Test
    fun testWhereNull() {
        //Given a range of where queries
        val null_1 = "DESCRIBE \".items\" WHERE \"title\" == NULL"
        val null_2 = "DESCRIBE \".items\" WHERE \"title\" != NULL"

        //When process
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(testJson)
        val null_1_query = null_1.toQuery()
        val null_2_query = null_2.toQuery()

        val null_1_result = jsonQuery.query(null_1)
        val null_2_result = jsonQuery.query(null_2)

        //Then check they're correct
        assertEquals("n1q field", "title", null_1_query.where!!.field)
        assertEquals("n1q operator", Query.Where.Operator.EQUAL, null_1_query.where.operator)
        assertEquals("n1q compare", NullCompare(), null_1_query.where.compare)

        assertEquals("n2q field", "title", null_2_query.where!!.field)
        assertEquals("n2q operator", Query.Where.Operator.NOT_EQUAL, null_2_query.where.operator)
        assertEquals("n2q compare", NullCompare(), null_2_query.where.compare)

        assertEquals("n1 result", "ARRAY(OBJECT(NUMBER, NULL), OBJECT(NUMBER, OBJECT(OBJECT(NUMBER, ARRAY(STRING[6])))))", null_1_result)
        assertEquals("n2 result", "ARRAY(OBJECT(NUMBER, STRING)[3])", null_2_result)
    }

    @Test
    fun testWhereMisc() {
        //Given a range of where queries
        val fields = "DESCRIBE \".items\" WHERE \"attr.history.pageCount\" > 0"
        val element = "DESCRIBE \".ages\" WHERE ELEMENT < 50"
        val target = "DESCRIBE \".user.usageIds\" WHERE ELEMENT > 10"

        //When process
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(testJson)
        val fields_query = fields.toQuery()
        val element_query = element.toQuery()
        val target_query = target.toQuery()

        val fields_result = jsonQuery.query(fields)
        val element_result = jsonQuery.query(element)
        val target_result = jsonQuery.query(target)

        //Then check they're correct
        assertEquals("fq field", "attr.history.pageCount", fields_query.where!!.field)
        assertEquals("fq operator", Query.Where.Operator.GREATER_THAN, fields_query.where.operator)
        assertEquals("fq compare", 0.0, fields_query.where.compare)

        assertEquals("eq field", ELEMENT, element_query.where!!.field)
        assertEquals("eq operator", Query.Where.Operator.LESS_THAN, element_query.where.operator)
        assertEquals("eq compare", 50.0, element_query.where.compare)

        assertEquals("tq field", ELEMENT, target_query.where!!.field)
        assertEquals("tq operator", Query.Where.Operator.GREATER_THAN, target_query.where.operator)
        assertEquals("tq compare", 10.0, target_query.where.compare)

        assertEquals("f result", "ARRAY(OBJECT(NUMBER, OBJECT(OBJECT(NUMBER, ARRAY(STRING[6])))))", fields_result)
        assertEquals("e result", "ARRAY(NUMBER[4])", element_result)
        assertEquals("t result", "ARRAY(NUMBER[5])", target_result)
    }
}