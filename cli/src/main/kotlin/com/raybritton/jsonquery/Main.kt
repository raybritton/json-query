package com.raybritton.jsonquery

import com.google.gson.Gson

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

    val result = run(jsonQuery, progArgs)

    if (progArgs.forApi) {
        val gson = Gson()
        val output = mutableMapOf<Any, Any>()
        if (result.first != null) {
            output["result"] = result.first!!
        } else {
            output["error"] = mutableMapOf("message" to (result.second ?: "Unknown error"))
            if (result.third != null) {
                (output["error"] as MutableMap<String, String>)["extra"] = result.third!!
            }
        }
        println(gson.toJson(output))
    } else {
        if (result.first != null) {
            println(result.first)
        } else {
            println(result.second)
            if (result.third != null) {
                println(result.third)
            }
        }
    }
}

private fun run(json: JsonQuery, args: ProgArgs): Triple<String?, String?, String?> {
    return try {
        val output = json.query(args.query)
        Triple(output, null, null)
    } catch (e: SyntaxException) {
        if (args.debug) {
            e.printStackTrace()
        }
        Triple(null, "Unable to parse query: " + e.message, e.extraInfo?.text)
    } catch (e: RuntimeException) {
        if (args.debug) {
            e.printStackTrace()
        }
        Triple(null, "Unable to run query: " + e.message, e.extraInfo?.text)
    }
}
