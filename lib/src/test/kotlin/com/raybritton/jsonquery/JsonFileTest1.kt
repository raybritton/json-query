package com.raybritton.jsonquery

import org.junit.Assert.assertEquals
import org.junit.Test

class JsonFileTest1 {
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
        val output2 = jsonQuery.query("SELECT \"type\" FROM \".properties.tags.items\"")

        //Then check results contain values
        assertEquals("output 1", "integer", output1)
        assertEquals("output 2", "string", output2)
    }

    @Test
    fun search() {
        //Given the json above
        val jsonQuery = JsonQuery()
        jsonQuery.loadJson(json)

        //When searching for keys ands values
        val output1 = jsonQuery.query("SEARCH \".\" FOR KEY \"type\"")
        val output2 = jsonQuery.query("SEARCH \".\" FOR VALUE \"string\"")
        val output3 = jsonQuery.query("SEARCH \".\" FOR VALUE \"Product\"")

        //Then check results are formatted corrrectly
        assertEquals("output 1", ".type: object\n" +
                ".properties.id.type: integer\n" +
                ".properties.name.type: string\n" +
                ".properties.price.type: number\n" +
                ".properties.tags.type: array\n" +
                ".properties.tags.items.type: string", output1)
        assertEquals("output 2", ".properties.name.type: string\n" +
                ".properties.tags.items.type: string", output2)
        assertEquals("output 3", ".title: Product", output3)
    }
}