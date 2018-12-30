package com.raybritton.jsonquery

import com.raybritton.jsonquery.parsing.tokens.Token

class SyntaxException(message: String, val extraInfo: ExtraInfo? = null) : IllegalStateException(message) {
    internal constructor(token: Token<*>, expected: String, extraInfo: ExtraInfo? = null) : this("Unexpected ${token::class.java.simpleName} \"${token.value}\" at ${token.charIdx}, expected $expected", extraInfo)

    companion object {
        internal fun throwNullable(token: Token<*>?, expected: String, extraInfo: ExtraInfo? = null): Nothing {
            if (token == null) {
                throw SyntaxException("Unexpected end of query, expected $expected", extraInfo)
            } else {
                throw SyntaxException(token, expected, extraInfo)
            }
        }
    }

    enum class ExtraInfo(val text: String) {
        METHOD_NOT_SET("All JQL statements must start with SELECT, DESCRIBE or SEARCH"),
        WITH("""WITH must be followed by KEYS or VALUES.
            |WITH KEYS is only valid on SELECT and not with AS JSON, it makes the keys be printed for all values
            |WITH VALUES is only valid on SEARCH and it makes the values be printed for all keys
        """.trimMargin()),
        JSON_TARGET("""
            Json target paths must be:
            - segmented by periods
            - must start with a period
            - must not be contain quote or double quotes unless they are part of the field name

            Each segment must contain at least 1 valid character
            Any periods in the name of a field must be escaped, i.e. \.

            Valid paths:
            . -> This selects the whole root object
            .foo -> This selects the field named 'foo' in the root object
            .[10] -> This selects the 10th element in the root array
            .bar.[1] -> This selects the 1st element in the array named 'bar' in the root object
            .foo.bar\. -> This selects the field named 'bar.' in the the object named 'foo' in the root object
            .[0 -> This selects the field named '[0' in the root object

            Invalid:
            ..foo -> Blank fields are not allowed
            .bar. -> A path must not end with a period
        """.trimIndent()),
        JSON_PATH("""
            Json projection paths must be:
            - segmented by periods
            - must not start with a period
            - must not be contain quote or double quotes unless they are part of the field name

            Each segment must contain at least 1 valid character
            Any periods in the name of a field must be escaped, i.e. \.

            Valid paths:
            foo -> This selects the field named 'foo' in the target
            [10] -> This selects the 10th element in the target
            bar.[1] -> This selects the 1st element in the array named 'bar' in the target
            foo.bar\. -> This selects the field named 'bar.' in the the object named 'foo' in the target
            [0 -> This selects the field named '[0' in the target

            Invalid:
            bar. -> A path must not end with a period
            ..foo -> Blank fields are not allowed
            . -> This is not allowed. To select all elements in an array write ELEMENT i.e. SELECT '.' WHERE ELEMENT == 'foo'
        """.trimIndent())
    }
}

