package com.raybritton.jsonquery.unit.printers

import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.printers.DescribePrinter
import org.junit.Assert.assertEquals
import org.junit.Test

class DescribePrinterTests {
    @Test
    fun `test simple object`() {
        val obj = JsonObject("a" to 78, "b" to "word", "c" to true, "d" to null)
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(), null)

        val result = DescribePrinter().print(obj, query)

        assertEquals("simple object", """OBJECT(NUMBER, STRING, BOOLEAN, NULL)""", result)
    }

    @Test
    fun `test simple array`() {
        val obj = JsonArray(-90, 10, "foo", false, null)
        val query = Query("", Query.Method.DESCRIBE, Target.TargetField("."), Query.Flags(), null)

        val result = DescribePrinter().print(obj, query)

        assertEquals("simple object", """ARRAY(NUMBER[2], STRING, BOOLEAN, NULL)""", result)
    }
}