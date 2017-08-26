package com.raybritton.jsonquery

import com.raybritton.jsonquery.models.Query
import java.util.Locale
import java.util.regex.Pattern

private val METHOD = "(DESCRIBE|GET|LIST)(.*)".toPattern(Pattern.CASE_INSENSITIVE) //Gets the method
private val TARGET = "\"((?:\\\\\"|[^\"])*)\"\\s*(KEYS|VALUES\\(.+\\)|VALUES)?(.*)".toPattern(Pattern.CASE_INSENSITIVE) //Gets the target and target modifiers
private val TARGET_KEYS = "\"((?:\\\\\"|[^\"])*)\"\\s*,?\\s*".toPattern(Pattern.CASE_INSENSITIVE) //Gets the keys from the VALUES target modifier
private val WHERE = "WHERE\\s+\"((?:\\\\\"|[^\"])*)\"\\s+([<>!=]+)\\s+(\\d+|\".+\")(.*)".toPattern(Pattern.CASE_INSENSITIVE) //Gets the where expression
private val SKIP = ".*SKIP (\\d+).*".toPattern(Pattern.CASE_INSENSITIVE) //Gets skip count
private val LIMIT = ".*LIMIT (\\d+).*".toPattern(Pattern.CASE_INSENSITIVE) //Gets limit count
private val WITH_KEYS = "WITH KEYS"
private val AS_JSON = "AS JSON"

internal fun String.toQuery(): Query {
    var query = this.trim()
    val methodMatcher = METHOD.matcher(query)
    val method: Query.Method
    val target: String
    val targetExtra: Query.TargetExtra?
    val targetKeys = mutableListOf<String>()
    var withKeys = false
    var isJson = false
    var where: Query.Where? = null
    var skip = 0
    var limit = 0

    if (methodMatcher.matches()) {
        method = Query.Method.valueOf(methodMatcher.group(1).toUpperCase(Locale.US))
    } else {
        throw IllegalArgumentException("Unable to parse query method")
    }

    query = methodMatcher.group(2).trim()

    val targetMatcher = TARGET.matcher(query)
    if (targetMatcher.matches()) {
        target = targetMatcher.group(1)
        if (targetMatcher.group(2) != null) {
            val extras = targetMatcher.group(2)
            if (extras == "KEYS") {
                targetExtra = Query.TargetExtra.KEY
            } else if (extras == "VALUES") {
                targetExtra = Query.TargetExtra.VALUES
            } else {
                targetExtra = Query.TargetExtra.SPECIFIC
                val keys = extras.substring(7, extras.length - 1)
                val keyMatchers = TARGET_KEYS.matcher(keys)
                while (keyMatchers.find()) {
                    targetKeys.add(keyMatchers.group())
                }
            }
        } else {
            targetExtra = null
        }
    } else {
        throw IllegalArgumentException("Unable to parse query targets")
    }

    query = targetMatcher.group(3).trim() ?: ""

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

    if (method == Query.Method.DESCRIBE) {
        val builder = StringBuilder()
        if (withKeys || isJson) {
            builder.append("Can't use WITH KEYS or IS JSON with DESCRIBE\n")
        }
        if (skip > 0 || limit > 0) {
            builder.append("Can't use SKIP or LIMIT with DESCRIBE\n")
        }
        if (where != null) {
            builder.append("Can't use WHERE with DESCRIBE")
        }
        if (builder.isNotEmpty()) {
            throw IllegalArgumentException(builder.toString())
        }
    }

    return Query(
            method = method,
            target = target,
            targetExtra = targetExtra,
            targetKeys = targetKeys,
            withKeys = withKeys,
            asJson = isJson,
            skip = skip,
            limit = limit,
            where = where
    )
}