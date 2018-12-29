package com.raybritton.jsonquery.models

class JsonObject : HashMap<String, Any?> {
    constructor() : super()
    constructor(collection: Map<String, Any?>) : super(collection)
    constructor(vararg content: Pair<String, Any?>) : super(mapOf(*content))

    fun copy() = JsonObject(this)
}