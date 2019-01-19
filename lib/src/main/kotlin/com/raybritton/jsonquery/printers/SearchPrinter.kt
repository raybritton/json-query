package com.raybritton.jsonquery.printers

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.SearchQuery

internal object SearchPrinter : Printer {
    override fun print(json: Any, query: Query): String {
        return json.print(query).joinToString("\n")
    }

    private fun Any.print(query: Query): List<String> {
        if (query.search == null) {
            return listOf()
        }
        return when (this) {
            is JsonObject -> this.print(query, "")
            is JsonArray -> this.print(query, "")
            else -> throw RuntimeException("${this.javaClass} encountered when printing search results")
        }
    }

    private fun JsonObject.print(query: Query, path: String): List<String> {
        val result = mutableListOf<String>()
        forEach { (key, value) ->
            when (value) {
                is JsonObject -> result += value.print(query, "$path.$key")
                is JsonArray -> result += value.print(query, "$path.$key")
                else -> {
                    when(query.search!!.targetRange) {
                        SearchQuery.TargetRange.ANY -> {
                            if (query.search.operator.op(key, query.search.value, query.flags.isCaseSensitive) ||
                                    query.search.operator.op(value, query.search.value, query.flags.isCaseSensitive)) {
                                if (query.flags.isWithValues) {
                                    result += "$path.$key: $value"
                                } else {
                                    result += "$path.$key"
                                }
                            }
                        }
                        SearchQuery.TargetRange.KEY -> {
                            if (query.search.operator.op(key, query.search.value, query.flags.isCaseSensitive)) {
                                if (query.flags.isWithValues) {
                                    result += "$path.$key: $value"
                                } else {
                                    result += "$path.$key"
                                }
                            }
                        }
                        SearchQuery.TargetRange.VALUE -> {
                            if (query.search.operator.op(value, query.search.value, query.flags.isCaseSensitive)) {
                                if (query.flags.isWithValues) {
                                    result += "$path.$key: $value"
                                } else {
                                    result += "$path.$key"
                                }
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    private fun JsonArray.print(query: Query, path: String): List<String> {
        val result = mutableListOf<String>()
        forEachIndexed { idx, value ->
            when (value) {
                is JsonObject -> result += value.print(query, "$path.[$idx]")
                is JsonArray -> result += value.print(query, "$path.[$idx]")
                else -> {
                    if (query.search!!.operator.op(value, query.search.value, query.flags.isCaseSensitive)) {
                        if (query.flags.isWithValues) {
                            result += "$path.[$idx]: $value"
                        } else {
                            result += "$path.[$idx]"
                        }
                    }
                }
            }
        }
        return result
    }
}