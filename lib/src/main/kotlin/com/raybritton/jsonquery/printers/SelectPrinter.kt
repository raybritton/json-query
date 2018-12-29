package com.raybritton.jsonquery.printers

import com.google.gson.GsonBuilder
import com.raybritton.jsonquery.ext.sort
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.SelectProjection
import com.raybritton.jsonquery.tools.navigateToProjection

internal class SelectPrinter : Printer {
    override fun print(json: Any, query: Query): String {
        return if (query.flags.isAsJson) {
            JsonPrinter().print(json, query)
        } else {
            json.print(query)
        }
    }

    private fun Any?.print(query: Query, isRoot: Boolean = false): String {
        if (this == null) return ""
        return when (this) {
            is JsonArray -> this.print(query, true)
            is JsonObject -> this.print(query, true)
            is Pair<*, *> -> {
                if (query.flags.isWithKeys) {
                    "${this.first}: ${this.second.wrap()}"
                } else {
                    this.second.wrap()
                }
            }
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

    private fun JsonObject.print(query: Query, isRoot: Boolean = false): String {
        if (isEmpty()) return "{}"
        val showMarkers = (size != 1) || !isRoot || query.flags.isWithKeys
        val builder = StringBuilder(if (showMarkers) "{" else "")
        for (key in keys) {
            val output = get(key).print(query, false)
            if (output.isNotBlank()) {
                if (query.flags.isWithKeys) {
                    builder.append(key)
                    builder.append(": ")
                }
                builder.append(output)
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

    private fun JsonArray.print(query: Query, isRoot: Boolean = false): String {
        this.sort(query)
        if (isEmpty()) return "[]"
        val showMarkers = (size != 1) || !isRoot
        val builder = StringBuilder(if (showMarkers) "[" else "")
        for (element in this) {
            val startingLength = builder.length
            when (query.select!!.projection) {
                is SelectProjection.SingleField -> {
                    val output = element?.navigateToProjection((query.select.projection as SelectProjection.SingleField).field).print(query, false)
                    if (output.isNotBlank()) {
                        builder.append(output)
                    }
                }
                is SelectProjection.MultipleFields -> {
                    builder.append("{")
                    for (key in (query.select.projection as SelectProjection.MultipleFields).fields) {
                        val output = element?.navigateToProjection(key).print(query, false)
                        if (output.isNotBlank()) {
                            builder.append(output)
                            builder.append(", ")
                        }
                    }
                    builder.setLength(builder.length - 2)
                    builder.append("}")
                }
                is SelectProjection.All -> builder.append(element.print(query, false))
            }
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
        }.create()

        return gson.toJson(json)
    }
}