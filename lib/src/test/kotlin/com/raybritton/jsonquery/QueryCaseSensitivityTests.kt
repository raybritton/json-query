package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Test

class QueryCaseSensitivityTests {
    @Test
    fun testSearch() {
        val json = "{'x':1}"

        val output1 = JsonQuery().loadJson(json).query("search '.' for 'x' case sensitive with values")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'x' CASE SENSITIVE WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEArCH '.' fOR 'x' CAse SENSitiVE WIth VALuES")

        Assert.assertEquals("Search 1", ".x: 1.0", output1)
        Assert.assertEquals("Search 2", ".x: 1.0", output2)
        Assert.assertEquals("Search 3", ".x: 1.0", output3)
    }
}