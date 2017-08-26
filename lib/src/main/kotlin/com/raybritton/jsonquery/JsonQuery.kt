package com.raybritton.jsonquery

import com.google.gson.Gson
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.utils.describe
import com.raybritton.jsonquery.utils.navigate

class JsonQuery {
    private lateinit var map: Map<String, Any>

    fun loadJson(path: String) {
        val json = JsonLoader().load(path)
        @Suppress("UNCHECKED_CAST") //This is ok as long as Gson doesn't change it's implementation
        map = Gson().fromJson(json, Any::class.java) as Map<String, Any>
        if (map.javaClass.typeName != "com.google.gson.internal.LinkedTreeMap") {
            throw IllegalStateException("Unable to parse json")
        }
    }

    fun query(queryStr: String): String {
        val query = queryStr.toQuery()
        when (query.method) {
            Query.Method.DESCRIBE -> return map.navigate(query.target).describe()
            Query.Method.LIST -> TODO()
            Query.Method.GET -> TODO()
        }
    }
}