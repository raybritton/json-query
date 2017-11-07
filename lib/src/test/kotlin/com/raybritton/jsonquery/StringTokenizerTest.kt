package com.raybritton.jsonquery

import com.raybritton.jsonquery.parsing.tokenize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@DisplayName("String Tokenizer")
class StringTokenizerTest {

    @Nested
    inner class `Character test` {
        @Test
        fun `whitespace test`() {
            val results = "     a   b  c    d".tokenize()
            assertEquals(4, results.size, "size")
            assertEquals("a", results[0], "token 1")
            assertEquals("b", results[1], "token 2")
            assertEquals("c", results[2], "token 3")
            assertEquals("d", results[3], "token 4")
        }
    }

    @Nested
    inner class `Simple query tests` {
        @Test
        fun `select everything`() {
            val results = """SELECT "."""".tokenize()
            assertEquals(2, results.size, "size")
            assertEquals("SELECT", results[0], "token 1")
            assertEquals("\".\"", results[1], "token 2")
        }

        @Test
        fun `select id from everything`() {
            val results = """SELECT "id" FROM "."""".tokenize()
            assertEquals(4, results.size, "size")
            assertEquals("SELECT", results[0], "token 1")
            assertEquals("\"id\"", results[1], "token 2")
            assertEquals("FROM", results[2], "token 3")
            assertEquals("\".\"", results[3], "token 4")
        }

        @Test
        fun `select id from everything where name equals string`() {
            val results = """SELECT "id" FROM "." WHERE "name" == "test" """.tokenize()
            assertEquals(8, results.size, "size")
            assertEquals("SELECT", results[0], "token 1")
            assertEquals("\"id\"", results[1], "token 2")
            assertEquals("FROM", results[2], "token 3")
            assertEquals("\".\"", results[3], "token 4")
            assertEquals("WHERE", results[4], "token 5")
            assertEquals("\"name\"", results[5], "token 6")
            assertEquals("==", results[6], "token 7")
            assertEquals("\"test\"", results[7], "token 8")
        }

        @Test
        fun `select id from everything where name equals number`() {
            val results = """SELECT "id" FROM "." WHERE "name" == 343 """.tokenize()
            assertEquals(8, results.size, "size")
            assertEquals("SELECT", results[0], "token 1")
            assertEquals("\"id\"", results[1], "token 2")
            assertEquals("FROM", results[2], "token 3")
            assertEquals("\".\"", results[3], "token 4")
            assertEquals("WHERE", results[4], "token 5")
            assertEquals("\"name\"", results[5], "token 6")
            assertEquals("==", results[6], "token 7")
            assertEquals("343", results[7], "token 8")
        }

        @Test
        fun `select id and name from everything as json`() {
            val results = """SELECT ("id", "name") FROM "." AS JSON """.tokenize()
            assertEquals(7, results.size, "size")
            assertEquals("SELECT", results[0], "token 1")
            assertEquals("\"id\"", results[1], "token 2")
            assertEquals("\"name\"", results[2], "token 3")
            assertEquals("FROM", results[3], "token 4")
            assertEquals("\".\"", results[4], "token 5")
            assertEquals("AS", results[5], "token 6")
            assertEquals("JSON", results[6], "token 7")
        }
    }

}