package com.raybritton.jsonquery

import com.raybritton.jsonquery.parsing.parse

/**
 * -q string : query
 * -i string : input file or json
 * -o string : output file (optional)
 */
fun main(args: Array<String>) {
    val tokens = "SELECT DISTINCT 1 23.2 'test' ".parse()
    println(tokens)


//    val progArgs = ProgArgs.build(args)
//    val jsonQuery = JsonQuery()
//    jsonQuery.loadJson(progArgs.input)
//    val output = jsonQuery.query(progArgs.query)
//    println(output)
}