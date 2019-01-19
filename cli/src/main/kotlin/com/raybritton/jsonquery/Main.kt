package com.raybritton.jsonquery

/**
 * -q string : query
 * -i string : input file or json
 * -o string : output file (optional)
 * -d : debug
 */
fun main(args: Array<String>) {
    val progArgs = ProgArgs.build(args)
    val jsonLoader = JsonLoader()
    val json = jsonLoader.load(progArgs.input)
    val jsonQuery = JsonQuery(json)
    try {
        val output = jsonQuery.query(progArgs.query)
        println(output)
    } catch (e: SyntaxException) {
        println("Unable to parse query: " + e.message)
        if (progArgs.debug) {
            e.printStackTrace()
        }
        println(e.extraInfo?.text)
    } catch (e: RuntimeException) {
        println("Unable to run query: " + e.message)
        if (progArgs.debug) {
            e.printStackTrace()
        }
        println(e.extraInfo?.text)
    }
}