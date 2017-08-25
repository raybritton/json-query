package com.raybritton.jsonquery

import com.raybritton.jsonquery.models.Query
import java.util.Locale
import java.util.regex.Pattern

class QueryParser() {
    fun parse(query: String): Query {
        var query = query.trim()
        val methodMatcher = METHOD_TARGET.matcher(query)
        val method: Query.Method
        val target: String
        var withKeys = false
        var isJson = false
        var where: Query.Where? = null
        var skip = 0
        var limit = 0

        if (methodMatcher.matches()) {
            method = Query.Method.valueOf(methodMatcher.group(1).toUpperCase(Locale.US))
            if (methodMatcher.group(2) != null) {
                target = methodMatcher.group(2).trim() + " " + methodMatcher.group(3)
            } else {
                target = methodMatcher.group(3)
            }
        } else {
            error("Unable to parse query method and/or target")
        }

        query = methodMatcher.group(4).trim()

        val whereMatcher = WHERE.matcher(query)
        if (whereMatcher.matches()) {
            var compare = whereMatcher.group(3)
            if (compare[0] == '"') {
                compare = compare.substring(1, compare.length - 1)
            }
            where = Query.Where(whereMatcher.group(1),
                    Query.Where.getOperatorBySymbol(whereMatcher.group(2)),
                    compare)
            query = whereMatcher.group(4).trim()
        }

        val skipMatcher = SKIP.matcher(query)
        if (skipMatcher.matches()) {
            skip = skipMatcher.group(1).toInt()
        }

        val limitMatcher = LIMIT.matcher(query)
        if (limitMatcher.matches()) {
            limit = limitMatcher.group(1).toInt()
        }

        withKeys = query.contains(WITH_KEYS, true)
        isJson = query.contains(AS_JSON, true)

        return Query(
                method = method,
                target = target,
                withKeys = withKeys,
                asJson = isJson,
                skip = skip,
                limit = limit,
                where = where
        )
    }

    companion object {
        private val METHOD_TARGET = "([a-zA-Z]{1,8})\\s+(VALUES\\s+|KEYS\\s+)?\"((?:\\\\\"|[^\"])*)\"(.*)".toPattern(Pattern.CASE_INSENSITIVE)
        private val WHERE = "WHERE\\s+\"((?:\\\\\"|[^\"])*)\"\\s+([<>!=]+)\\s+(\\d+|\".+\")(.*)".toPattern(Pattern.CASE_INSENSITIVE)
        private val SKIP = ".*SKIP (\\d+).*".toPattern(Pattern.CASE_INSENSITIVE)
        private val LIMIT = ".*LIMIT (\\d+).*".toPattern(Pattern.CASE_INSENSITIVE)
        private val WITH_KEYS = "WITH KEYS"
        private val AS_JSON = "AS JSON"
    }
}