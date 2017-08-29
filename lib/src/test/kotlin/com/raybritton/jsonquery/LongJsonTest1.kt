package com.raybritton.jsonquery

import org.junit.Assert
import org.junit.Test

class LongJsonTest1 {
    val json = """
        {
            "${'$'}schema": "http://json-schema.org/draft-06/schema#",
            "title": "Product",
            "description": "A product from Acme's catalog",
            "type": "object",
            "properties": {
                "id": {
                    "description": "The unique identifier for a product",
                    "type": "integer"
                },
                "name": {
                    "description": "Name of the product",
                    "type": "string"
                },
                "price": {
                    "type": "number",
                    "exclusiveMinimum": 0
                },
                "tags": {
                    "type": "array",
                    "items": {
                        "type": "string"
                    },
                    "minItems": 1,
                    "uniqueItems": true
                }
            },
            "required": ["id", "name", "price"]
        }
    """.trimIndent()

    @Test
    fun listDeepValues() {
        //Given the json above
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)

        //When several select queries retrieving deep values are run
        val output1 = jsonQuery.query("SELECT \"type\" FROM \".properties.id\"")

        //Then check results contain values
        Assert.assertEquals("output 1", "integer", output1)
    }
}