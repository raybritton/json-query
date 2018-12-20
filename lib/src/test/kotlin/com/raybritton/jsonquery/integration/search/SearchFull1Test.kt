package com.raybritton.jsonquery.integration.search

import com.raybritton.jsonquery.JsonQuery
import junit.framework.Assert.assertEquals
import org.junit.Test

class SearchFull1Test {
    private val json = """
        [{
          "_id": {
            "oid": "5968dd23fc13ae04d9000001"
          },
          "product_name": "sildenafil citrate",
          "supplier": "Wisozk Inc",
          "quantity": 261,
          "unit_cost": "10.47",
          "origin": "supplier"
        }, {
          "_id": {
            "oid": "5968dd23fc13ae04d9000002"
          },
          "product_name": "Mountain Juniperus ashei",
          "supplier": "Keebler-Hilpert",
          "quantity": 292,
          "unit_cost": "8.74",
          "origin": "supplier"
        }, {
          "_id": {
            "oid": "5968dd23fc13ae04d9000003"
          },
          "product_name": "Dextromathorphan HBr",
          "supplier": "Schmitt-Weissnat",
          "quantity": 211,
          "unit_cost": "20.53",
          "origin": "sample"
        }]
    """.trimIndent()

    @Test
    fun wideSearch() {
        val output1 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '261'")
        val output2 = JsonQuery().loadJson(json).query("SEARCH '.' FOR VALUE '261' WITH VALUES")
        val output3 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'unit_cost'")
        val output4 = JsonQuery().loadJson(json).query("SEARCH '.' FOR KEY 'unit_cost' WITH VALUES")
        val output5 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'supplier'")
        val output6 = JsonQuery().loadJson(json).query("SEARCH '.' FOR 'supplier' WITH VALUES")

        assertEquals("Search 1", ".[0].quantity", output1)
        assertEquals("Search 2", ".[0].quantity: 261.0", output2)
        assertEquals("Search 3", ".[0].unit_cost\n.[1].unit_cost\n.[2].unit_cost", output3)
        assertEquals("Search 4", ".[0].unit_cost: 10.47\n.[1].unit_cost: 8.74\n.[2].unit_cost: 20.53", output4)
        assertEquals("Search 5", ".[0].supplier\n.[0].origin\n.[1].supplier\n.[1].origin\n.[2].supplier", output5)
        assertEquals("Search 6", ".[0].supplier: Wisozk Inc\n.[0].origin: supplier\n.[1].supplier: Keebler-Hilpert\n.[1].origin: supplier\n.[2].supplier: Schmitt-Weissnat", output6)
    }
}