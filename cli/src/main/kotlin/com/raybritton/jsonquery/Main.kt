package com.raybritton.jsonquery

/**
 * -q string : query
 * -i string : input file or json
 * -o string : output file (optional)
 */
fun main(args: Array<String>) {
    val progArgs = ProgArgs.build(args)
    val jsonQuery = JsonQuery()
    jsonQuery.loadJson(progArgs.input)
    val output = jsonQuery.query(progArgs.query)
    println(output)
}