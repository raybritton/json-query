package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.ProgArgs

/**
 * -q string : query
 * -i string : input file or json
 * -o string : output file (optional)
 */
fun main(args: Array<String>) {
    val progArgs = ProgArgs.build(args)
    val json = JsonLoader().load(progArgs.input)
    val map = Gson().fromJson(json, Any::class.java)
    if (map.javaClass.typeName == "com.google.gson.internal.LinkedTreeMap") {
        val query = QueryParser().parse(progArgs.query)
        val output = JsonQuery(map as Map<String, Any>).query(query)
        println(output)
    } else {
        error("JSON too simple or complex to be parsed")
    }
}