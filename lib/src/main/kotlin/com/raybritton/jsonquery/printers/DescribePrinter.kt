package com.raybritton.jsonquery.printers

import com.raybritton.jsonquery.ext.isValue
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query

internal class DescribePrinter : Printer {
    override fun print(json: Any, query: Query): String {
        return json.describe(query)
    }

    private fun Any?.describe(query: Query, tabCount: Int = 0): String {
        if (this == null) {
            return "NULL"
        }
        return when (this) {
            is Int -> "NUMBER"
            is Long -> "NUMBER"
            is Float -> "NUMBER"
            is Double -> "NUMBER"
            is String -> "STRING"
            is Boolean -> "BOOLEAN"
            is JsonObject -> this.describe(query, tabCount)
            is JsonArray -> this.describe(query, tabCount)
            else -> "UNKNOWN"
        }
    }

    private fun JsonObject.describe(query: Query, tabCount: Int = 0): String {
        val builder = StringBuilder("OBJECT(")
        values.toList().describe(query, builder, tabCount + 1)
        return builder.append(")").toString()
    }

    private fun JsonArray.describe(query: Query, tabCount: Int = 0): String {
        val builder = StringBuilder("ARRAY(")
        describe(query, builder, tabCount + 1)
        return builder.append(")").toString()
    }

    private fun List<*>.describe(query: Query, builder: StringBuilder, tabCount: Int = 0) {
        val map = mutableMapOf<String, Int>()
        for (i in 0 until size) {
            val desc = this[i].describe(query, tabCount)
            if (map.containsKey(desc)) {
                map[desc] = map[desc]!! + 1
            } else {
                map[desc] = 1
            }
        }
        if (map.isNotEmpty()) {
            val expand = query.flags.isPrettyPrinted && (map.size > 1 || !this[0].isValue())
            for ((key, value) in map) {
                if (expand) {
                    builder.append("\n")
                    for (i in 0 until tabCount) {
                        builder.append(TAB)
                    }
                }
                if (value > 1) {
                    builder.append(key)
                    builder.append("[")
                    builder.append(value)
                    builder.append("]")
                } else {
                    builder.append(key)
                }
                builder.append(", ")
            }
            builder.setLength(builder.length - 2)
        }
    }

    companion object {
        private const val TAB = "  "
    }
}