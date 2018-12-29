package com.raybritton.jsonquery.parsing.tokens

import java.util.*

/**
 * The commands of JQL
 *
 * Commands like AS JSON are read as two separate commands but handled as one
 */

internal enum class Keyword {
    SELECT,
    SEARCH,
    WHERE,
    ELEMENT,
    CASE,
    SENSITIVE,
    FOR,
    ANY,
    KEY,
    VALUE,
    LIMIT,
    OFFSET,
    ORDER,
    BY,
    DESC,
    TRUE,
    FALSE,
    KEYS,
    VALUES,
    SUM,
    MIN,
    MAX,
    COUNT,
    AS,
    JSON,
    DISTINCT,
    FROM,
    WITH,
    DESCRIBE,
    PRETTY,
    NULL;

    fun isMath() = (this == MIN || this == MAX || this == SUM || this == COUNT)

    companion object {
        fun isValid(keyword: String) = values().any { it.name == keyword.toUpperCase(Locale.US) }
    }
}