package com.raybritton.jsonquery

class RuntimeException(message: String, val extraInfo: RuntimeException.ExtraInfo? = null) : IllegalStateException(message) {

    enum class ExtraInfo(val text: String) {
        NAVIGATED_NULL("""The target was null, only non null objects or arrays can be targets
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        NAVIGATED_VALUE("""The target was a value (such as a number or string) only objects or arrays can be targets
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        NAVIGATED_NON_VALUE("""The last segment in the path was not a value, only numbers, strings or booleans can be projections
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        INDEX_ON_OBJECT("""The last segment in the path was an index accessor (e.g. [1]) but the json container was an object
        |Only field accessors (e.g. .bar) are allowed for objects
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        INDEX_ON_VALUE("""The segment in the path was an index accessor (e.g. [1]) but the json was value and not a json array
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        FIELD_ON_ARRAY("""The last segment in the path was an field accessor (e.g. .foo) but the json container was an array
        |If the array contains at least one object you can use field accessors to access the fields in those objects but otherwise only index accessors (e.g. [10] are allowed for arrays)
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        INDEX_TOO_HIGH("""The index was greater than or equal to the array length
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        NAVIGATED_NOTHING("""No field with that name was found in the object
        |If using a json path as the target check the spelling and escaping
        |If using a nested query try running it by itself to check the result first
    """.trimMargin()),
        OFFSET_OBJECT("""OFFSET only works when the target is an array.
            |OFFSET is run after navigating to the specified target
        """.trimMargin()),
        LIMIT_OBJECT("""LIMIT only works when the target is an array.
            |LIMIT is run after navigating to the specified target and removing any amount specified by OFFSET
        """.trimMargin()),
        SEARCH_QUERY("""A nested query for search didn't parse
            |Try running it by itself
        """.trimMargin()),
        WHERE_QUERY("""A nested query for where didn't parse
            |Try running it by itself
        """.trimMargin()),
        ELEMENT_WHERE_OBJECT("""Using ELEMENT with WHERE only works if the target is an array"""),
        WHERE_INVALID("""Using contains (#) or not contains (!#) with WHERE only works if the target is an array, object or string""")
    }
}