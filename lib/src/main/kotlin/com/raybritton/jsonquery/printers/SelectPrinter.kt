package com.raybritton.jsonquery.printers

import com.google.gson.GsonBuilder
import com.raybritton.jsonquery.ext.isValue
import com.raybritton.jsonquery.ext.sort
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.SelectProjection
import com.raybritton.jsonquery.tools.navigateToProjection

internal object SelectPrinter : Printer {
    override fun print(json: Any, query: Query): String {
        return if (query.flags.isAsJson) {
            JsonPrinter().print(json, query)
        } else {
            json.printSelect(query)
        }
    }

    private fun Any?.printSelect(query: Query): String {
        if (this == null) return "null"

        return when (this) {
            is JsonArray -> this.print(query)
            is JsonObject -> this.print(query)
            else -> this.wrap()
        }
    }

    private fun Any?.wrap(): String {
        if (this is String) {
            return '"' + this + '"'
        } else {
            return this.toString()
        }
    }

    private fun JsonObject.print(query: Query): String {
        if (isEmpty()) return "{}"
        val showMarkers = (size != 1)
        val builder = StringBuilder(if (showMarkers) "{" else "")
        for (key in keys) {
            val output = get(key).printSelect(query)
            if (output.isNotBlank()) {
                when {
                    query.flags.isOnlyPrintKeys -> builder.append(key)
                    query.flags.isOnlyPrintValues -> builder.append(output)
                    else -> {
                        if (get(key).isValue() || (query.select?.projection == SelectProjection.All)) {
                            builder.append(key)
                            builder.append(": ")
                        }
                        builder.append(output)
                    }
                }
                builder.append(", ")
            }
        }
        if (builder.length > 1) {
            builder.setLength(builder.length - 2)
        }
        if (showMarkers) {
            builder.append("}")
        }
        return builder.toString()
    }

    private fun JsonArray.print(query: Query): String {
        this.sort(query)
        if (isEmpty()) return "[]"
        val showMarkers = (size != 1)
        val builder = StringBuilder(if (showMarkers) "[" else "")
        for (element in this) {
            val startingLength = builder.length
            builder.append(element.printSelect(query))
            if (builder.length > startingLength) {
                builder.append(", ")
            }
        }
        if (builder.length > 1) {
            builder.setLength(builder.length - 2)
        }
        if (showMarkers) {
            builder.append("]")
        }
        return builder.toString()
    }
}

private class JsonPrinter : Printer {
    override fun print(json: Any, query: Query): String {
        val gson = GsonBuilder().apply {
            if (query.flags.isPrettyPrinted) {
                this.setPrettyPrinting()
            }
            this.serializeNulls()
        }.create()

        return gson.toJson(json)
    }
}