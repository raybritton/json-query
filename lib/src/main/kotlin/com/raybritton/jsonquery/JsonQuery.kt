package com.raybritton.jsonquery

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import com.raybritton.jsonquery.ext.sort
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.printer.describe
import com.raybritton.jsonquery.tools.filter
import com.raybritton.jsonquery.printer.list
import com.raybritton.jsonquery.tools.filterToKeys
import com.raybritton.jsonquery.tools.navigate
import com.raybritton.jsonquery.tools.search
import com.raybritton.jsonquery.tools.toQuery

typealias JsonObject = LinkedTreeMap<*, *>
typealias JsonArray = ArrayList<*>

class JsonQuery {
    private lateinit var json: String
    private val gsonBuilderProvider: () -> GsonBuilder = { GsonBuilder() }

    fun loadJson(path: String) {
        json = JsonLoader().load(path)
    }

    fun query(queryStr: String): String {
        val query = queryStr.toQuery()
        return query(query)
    }

    fun query(query: Query): String {
        val gson = gsonBuilderProvider().let {
            if (query.pretty) {
                it.setPrettyPrinting()
            }
            it.create()
        }

        @Suppress("UNCHECKED_CAST") //This is ok as long as Gson doesn't change it's implementation
        //In particular arrays must be represented as ArrayList and objects as LinkedTreeMap
        val jsonObj = gson.fromJson(json, Any::class.java)

        val filtered = jsonObj.navigate(query.target).filter(query)

        when (query.method) {
            Query.Method.DESCRIBE -> return filtered.describe(query)
            Query.Method.SELECT -> {
                if (query.asJson) {
                    return gson.toJson(filtered.sort(query).filterToKeys(query))
                } else {
                    return filtered.list(query)
                }
            }
            Query.Method.SEARCH -> {
                return filtered.search(query, query.target).joinToString("\n")
            }
        }
    }
}