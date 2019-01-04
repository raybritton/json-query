package com.raybritton.jsonquery.unit.filtering

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.tools.offset
import org.junit.Assert.assertEquals
import org.junit.Test

class FilteringTests {
    @Test
    fun `test offset`() {
        val json = JsonArray(1, 2, 3, 4, 5)

        val result = json.offset(2)

        assertEquals(JsonArray(3, 4, 5), result)
    }

}