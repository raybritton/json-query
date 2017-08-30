package com.raybritton.jsonquery

import com.google.gson.GsonBuilder
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.printer.describe
import com.raybritton.jsonquery.tools.filter
import com.raybritton.jsonquery.printer.list
import com.raybritton.jsonquery.tools.navigate
import com.raybritton.jsonquery.tools.toQuery

class JsonQuery {
    private lateinit var json: String
    private val gsonBuilderProvider: () -> GsonBuilder = { GsonBuilder() }

    fun loadJson(path: String) {
        json = JsonLoader().load(path)
    }

    fun query(queryStr: String): String {
        val query = queryStr.toQuery()
        val gson = gsonBuilderProvider().let {
            if (query.pretty) {
                it.setPrettyPrinting()
            }
            it.create()
        }

        @Suppress("UNCHECKED_CAST") //This is ok as long as Gson doesn't change it's implementation
        val jsonObj = gson.fromJson(json, Any::class.java)

        val filtered = jsonObj.navigate(query.target).filter(query)

        when (query.method) {
            Query.Method.DESCRIBE -> return filtered.describe()
            Query.Method.SELECT, Query.Method.DISTINCT -> {
                if (query.asJson) {
                    return gson.toJson(filtered)
                } else {
                    return filtered.list(query)
                }
            }
        }
    }
}