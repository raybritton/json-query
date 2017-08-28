package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.printer.describe
import com.raybritton.jsonquery.tools.filter
import com.raybritton.jsonquery.printer.list
import com.raybritton.jsonquery.tools.navigate
import com.raybritton.jsonquery.tools.toQuery

class JsonQuery {
    private lateinit var json: String
    private val gson = Gson()

    fun loadJson(path: String) {
        json = JsonLoader().load(path)
    }

    fun query(queryStr: String): String {
        @Suppress("UNCHECKED_CAST") //This is ok as long as Gson doesn't change it's implementation
        val jsonObj = gson.fromJson(json, Any::class.java)
        val query = queryStr.toQuery()
        val filtered = jsonObj.navigate(query.target).filter(query)
        when (query.method) {
            Query.Method.DESCRIBE -> return filtered.describe()
            Query.Method.SELECT -> {
                if (query.asJson) {
                    return gson.toJson(filtered)
                } else {
                    return filtered.list(query)
                }
            }
        }
    }
}