package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.printer.describe
import com.raybritton.jsonquery.tools.filter
import com.raybritton.jsonquery.printer.list
import com.raybritton.jsonquery.tools.navigate
import com.raybritton.jsonquery.tools.toQuery

class JsonQuery {
    private lateinit var jsonObj: Any

    fun loadJson(path: String) {
        val json = JsonLoader().load(path)
        @Suppress("UNCHECKED_CAST") //This is ok as long as Gson doesn't change it's implementation
        jsonObj = Gson().fromJson(json, Any::class.java)
    }

    fun query(queryStr: String): String {
        val query = queryStr.toQuery()
        val filtered = jsonObj.navigate(query.target).filter(query)
        when (query.method) {
            Query.Method.DESCRIBE -> return filtered.describe()
            Query.Method.LIST -> return filtered.list(query)
            Query.Method.GET -> TODO()
        }
    }
}