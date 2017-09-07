package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.utils.ELEMENT
import java.util.Locale
import java.util.regex.Pattern

/**
 * Method and target if searching
 * Group 1: target
 * Group 2: target modifier
 * Group 3: target key
 * Group 4: remaining
 */
private val SEARCH = "SEARCH\\s+\"((?:\\\\\"|[^\"])*)\"\\s+FOR\\s+(KEY|VALUE)\\s+\"((?:\\\\\"|[^\"])*)\"(.+)?".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the method
 * Group 1: Method
 * Group 2: Distinct
 * Group 3: Remaining
 */
private val METHOD = "(DESCRIBE|SELECT)(?:\\s+(DISTINCT)\\s+)?(.*)".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the target, keys and target extras
 * Group 1: Keys or extras
 * Group 2: Target
 * Group 3: Remaining
 */
private val TARGET = "(?:((?:KEYS|VALUES|MAX\\((?:ELEMENT|\".+\")\\)|MIN\\((?:ELEMENT|\".+\")\\)|COUNT\\((?:ELEMENT|\".+\")\\)|SUM\\((?:ELEMENT|\".+\")\\)|(?:\\(.+\\)|\".+\")))\\s+FROM\\s+)?\"((?:\\\\\"|[^\"])*)\"(.*)".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the keys from the keys in TARGET
 * Call find() repeatedly on group 1 from TARGET
 */
private val TARGET_KEYS = "\"((?:\\\\\"|[^\"])*)\"\\s*,?\\s*".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Checks for (ELEMENT) match
 */
private val TARGET_ELEMENT = "[a-z]{3,5}\\(ELEMENT\\)".toPattern(Pattern.CASE_INSENSITIVE)
/**
 * Gets the where expression
 * Group 1: target
 * Group 2: operator
 * Group 3: compare
 * Group 4: remaining
 */
private val WHERE = "WHERE\\s+(\"(?:\\\\\"|[^\"])*\"|ELEMENT)\\s+([<>!=#]+)\\s+(NULL|-?\\d+(?:\\.)?e?(?:\\d+)?|\".+\")(.*)".toPattern(Pattern.CASE_INSENSITIVE)
private val OFFSET = ".*OFFSET (\\d+).*".toPattern(Pattern.CASE_INSENSITIVE) //Gets skip count
private val LIMIT = ".*LIMIT (\\d+).*".toPattern(Pattern.CASE_INSENSITIVE) //Gets limit count
/**
 * Gets the order by column/element
 * Group 1: column name or element
 * Group 2: desc (or not)
 */
private val ORDER_BY = ".*ORDER\\s+BY\\s+(ELEMENT|\"(?:\\\\\"|[^\"])*\").*".toPattern(Pattern.CASE_INSENSITIVE)
private val WITH_KEYS = "WITH KEYS"
private val AS_JSON = "AS JSON"
private val PRETTY = "PRETTY"
private val DESC = "DESC"

fun String.toQuery(): Query {
    var query = this.trim()

    val searchMatcher = SEARCH.matcher(query)
    if (searchMatcher.matches()) {
        query = searchMatcher.group(4) ?: ""
        val whereRemaining = query.where()
        query = whereRemaining.second

        return Query(
                method = Query.Method.SEARCH,
                target = searchMatcher.group(1),
                targetExtra = Query.TargetExtra.valueOf(searchMatcher.group(2)),
                targetKeys = listOf(searchMatcher.group(3)),
                asJson = query.contains(AS_JSON, true),
                offset = query.offset(),
                pretty = query.contains(PRETTY, true),
                limit = query.limit(),
                where = whereRemaining.first)
    } else {
        return selectOrDescribe(query)
    }
}

private fun String.order(): String? {
    val orderMatcher = ORDER_BY.matcher(this)
    if (orderMatcher.matches()) {
        return orderMatcher.group(1)
    } else {
        return null
    }
}

private fun String.limit(): Int? {
    val limitMatcher = LIMIT.matcher(this)
    if (limitMatcher.matches()) {
        return limitMatcher.group(1).toInt()
    } else {
        return null
    }
}

private fun String.offset(): Int? {
    val offsetMatcher = OFFSET.matcher(this)
    if (offsetMatcher.matches()) {
        return offsetMatcher.group(1).toInt()
    } else {
        return null
    }
}

private fun String.where(): Pair<Query.Where?, String> {
    val whereMatcher = WHERE.matcher(this)
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
        val where = Query.Where(field,
                Query.Where.getOperatorBySymbol(whereMatcher.group(2)),
                strCompare ?: boolCompare ?: numCompare)
        val remaining = whereMatcher.group(4)?.trim() ?: ""
        return where to remaining
    }
    return null to this
}

private fun selectOrDescribe(query: String): Query {
    var query = query.trim()
    val methodMatcher = METHOD.matcher(query)
    val method: Query.Method
    val target: String
    val targetExtra: Query.TargetExtra?
    val targetKeys = mutableListOf<String>()
    var distinct = false

    if (methodMatcher.matches()) {
        method = Query.Method.valueOf(methodMatcher.group(1).toUpperCase(Locale.US))
        if (methodMatcher.group(2).equals("DISTINCT", true)) {
            distinct = true
        }
    } else {
        throw IllegalArgumentException("Unable to parse query method")
    }

    query = methodMatcher.group(3).trim()

    val addAllKeys: (String) -> Unit = { extras ->
        if (TARGET_ELEMENT.matcher(extras).matches()) {
            targetKeys.add(ELEMENT)
        } else {
            val keyMatchers = TARGET_KEYS.matcher(extras)
            while (keyMatchers.find()) {
                val key = keyMatchers.group(1)
                targetKeys.add(key)
            }
        }
    }

    val targetMatcher = TARGET.matcher(query)
    if (targetMatcher.matches()) {
        target = targetMatcher.group(2)
        if (targetMatcher.group(1) != null) {
            val extras = targetMatcher.group(1)
            if (extras == "KEYS") {
                targetExtra = Query.TargetExtra.KEYS
            } else if (extras == "VALUES") {
                targetExtra = Query.TargetExtra.VALUES
            } else if (extras.startsWith("MAX")) {
                targetExtra = Query.TargetExtra.MAX
                addAllKeys(extras)
            } else if (extras.startsWith("MIN")) {
                targetExtra = Query.TargetExtra.MIN
                addAllKeys(extras)
            } else if (extras.startsWith("COUNT")) {
                targetExtra = Query.TargetExtra.COUNT
                addAllKeys(extras)
            } else if (extras.startsWith("SUM")) {
                targetExtra = Query.TargetExtra.SUM
                addAllKeys(extras)
            } else {
                targetExtra = Query.TargetExtra.SPECIFIC
                addAllKeys(extras)
            }
        } else {
            targetExtra = null
        }
    } else {
        throw IllegalArgumentException("Unable to parse query targets")
    }

    query = targetMatcher.group(3)?.trim() ?: ""

    val whereRemaining = query.where()
    query = whereRemaining.second

    return Query(
            method = method,
            target = target,
            targetExtra = targetExtra,
            targetKeys = targetKeys,
            withKeys = query.contains(WITH_KEYS, true),
            asJson = query.contains(AS_JSON, true),
            offset = query.offset(),
            pretty = query.contains(PRETTY, true),
            limit = query.limit(),
            where = whereRemaining.first,
            order = query.order(),
            desc = query.contains(DESC, true),
            distinct = distinct)
}