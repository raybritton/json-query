package com.raybritton.jsonquery.models

import com.google.gson.internal.LinkedTreeMap

class JsonObject : HashMap<String, Any?> {
    constructor() : super()
    constructor(collection: Map<String, Any?>) : super(collection)
    constructor(vararg content: Pair<String, Any?>) : super(mapOf(*content))

    fun copy() = JsonObject(this)

    companion object {
        fun fromLinkedTreeMap(map: LinkedTreeMap<*, *>): JsonObject {
            val obj = JsonObject()
            map.forEach { (key, value) -> obj[key.toString()] = value }
            return obj
        }
    }
}