package com.raybritton.jsonquery

import com.raybritton.jsonquery.models.Query
import java.util.Locale
import java.util.regex.Pattern

/**
 * Gets the method
 * Group 1: Method
 * Group 2: Remaining
 */
private val METHOD = "(DESCRIBE|GET|LIST)(.*)".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the target, keys and target extras
 * Group 1: Keys or extras
 * Group 2: Target
 * Group 3: Remaining
 */
private val TARGET = "(?:((?:KEYS|VALUES|(?:\\(.+\\)|\".+\")))\\s+IN\\s+)?\"((?:\\\\\"|[^\"])*)\"(.*)".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the keys from the keys in TARGET
 * Call find() repeatedly on group 1 from TARGET
 */
private val TARGET_KEYS = "\"((?:\\\\\"|[^\"])*)\".+".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the where expression
 * Group 1: target
 * Group 2: operator
 * Group 3: compare
 * Group 4: remaining
 */
private val WHERE = "WHERE\\s+(\"(?:\\\\\"|[^\"])*\"|ELEMENT)\\s+([<>!=#]+)\\s+(NULL|-?\\d+(?:\\.)?e?(?:\\d+)?|\".+\")(.*)".toPattern(Pattern.CASE_INSENSITIVE)
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
    var skip: Int? = null
    var limit: Int? = null

    if (methodMatcher.matches()) {
        method = Query.Method.valueOf(methodMatcher.group(1).toUpperCase(Locale.US))
    } else {
        throw IllegalArgumentException("Unable to parse query method")
    }

    query = methodMatcher.group(2).trim()

    val targetMatcher = TARGET.matcher(query)
    if (targetMatcher.matches()) {
        target = targetMatcher.group(2)
        if (targetMatcher.group(1) != null) {
            val extras = targetMatcher.group(1)
            if (extras == "KEYS") {
                targetExtra = Query.TargetExtra.KEY
            } else if (extras == "VALUES") {
                targetExtra = Query.TargetExtra.VALUES
            } else {
                targetExtra = Query.TargetExtra.SPECIFIC
                val keyMatchers = TARGET_KEYS.matcher(extras)
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
        val compare = whereMatcher.group(3)
        var boolCompare: Boolean? = null
        var strCompare: String? = null
        var numCompare: Double? = null
        if (compare == null || compare.equals("NULL", true)) {
            //nothing
        } else if (compare[0] == '"') {
            strCompare = compare.substring(1, compare.length - 1)
        } else if (compare.equals("true", true) || compare.equals("false", true)) {
            boolCompare = compare.toBoolean()
        } else {
            numCompare = compare.toDouble()
        }
        var field = whereMatcher.group(1)
        if (field.startsWith("\"")) {
            field = field.substring(1, field.length - 1)
        }
        where = Query.Where(field,
                Query.Where.getOperatorBySymbol(whereMatcher.group(2)),
                strCompare ?: boolCompare ?: numCompare)
        query = whereMatcher.group(4)?.trim() ?: ""
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
            targetExtra = targetExtra,
            targetKeys = targetKeys,
            withKeys = withKeys,
            asJson = isJson,
            skip = skip,
            limit = limit,
            where = where
    )
}