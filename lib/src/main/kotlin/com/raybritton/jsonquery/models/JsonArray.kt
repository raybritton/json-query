package com.raybritton.jsonquery.models

internal class JsonArray : ArrayList<Any?> {
    constructor() : super()
    constructor(collection: Collection<Any?>) : super(collection)
    constructor(vararg content: Any?) : super(listOf(*content))

    fun copy() = JsonArray(this)
}

internal fun List<*>.toJsonArray(): JsonArray {
    return JsonArray(this)
}