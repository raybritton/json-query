package com.raybritton.jsonquery

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.parsing.query.QueryExecutor
import com.raybritton.jsonquery.parsing.query.buildQuery
import com.raybritton.jsonquery.parsing.tokens.toQueryTokens
import com.raybritton.jsonquery.printers.Printer

class JsonQuery(private val json: String) {

    fun query(queryStr: String): String {
        JQLogger.debug("Input: $queryStr")
        val tokens = queryStr.toQueryTokens()
        JQLogger.debug("Tokens: " + tokens.joinToString())
        val query = tokens.buildQuery(queryStr)
        JQLogger.debug("Parsed as $query")
        return query(query)
    }

    private fun query(query: Query): String {
        var jsonObj = Gson().fromJson(json, Any::class.java)

        jsonObj = jsonObj.convertToInternalFormat()

        val result = QueryExecutor().execute(jsonObj, query)
        val printer = Printer.createPrinter(query)

        return printer.print(result, query)
    }

    private fun Any?.convertToInternalFormat(): Any? {
        return when (this) {
            is LinkedTreeMap<*, *> -> {
                val obj = JsonObject.fromLinkedTreeMap(this)
                for (key in obj.keys) {
                    obj[key] = obj[key].convertToInternalFormat()
                }
                obj
            }
            is ArrayList<*> -> {
                val list = JsonArray(this)
                (0 until list.size).forEach { idx ->
                    list[idx] = list[idx].convertToInternalFormat()
                }
                list
            }
            else -> this
        }
    }
}