package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchExclusionTests {
    @Test
    fun testObjectsInObject() {
        val json = "{'a':{'id':45, 'title':'Book 1', codes: [1, 2, 3]},'b':{'id':46, 'title':'Book 2', codes: [1, 2, 4]},'c':{'id':47, 'title':'Book 3', codes: [1, 7, 8]}}"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'id'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.a' FOR KEY 'id'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.b' FOR KEY 'id'")

        assertEquals("Search 1", ".a.id\n.b.id\n.c.id", output1)
        assertEquals("Search 2", ".a.id", output2)
        assertEquals("Search 3", ".b.id", output3)
    }

    @Test
    fun testObjectsInArray() {
        val json = "[{'id':45, 'title':'Book 1', codes: [1, 2, 3]},{'id':46, 'title':'Book 2', codes: [1, 2, 4]},{'id':47, 'title':'Book 3', codes: [1, 7, 8]}]"

        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'id'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.[0]' FOR KEY 'id'")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.[1]' FOR KEY 'id'")

        assertEquals("Search 1", ".[0].id\n.[1].id\n.[2].id", output1)
        assertEquals("Search 2", ".[0].id", output2)
        assertEquals("Search 3", ".[1].id", output3)
    }
}