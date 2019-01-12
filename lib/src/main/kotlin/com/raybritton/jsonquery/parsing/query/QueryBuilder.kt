package com.raybritton.jsonquery.parsing.query

import com.raybritton.jsonquery.SyntaxException
import com.raybritton.jsonquery.ext.joinStringOr
import com.raybritton.jsonquery.ext.toSegments
import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.tokens.Operator

/**
 * Used by QueryParser to build the query
 *
 * It checks that values set correctly
 *
 */

internal class QueryBuilder(val queryString: String) {
    var method: Query.Method? = null
        set(value) {
            checkMethodNotSet()
            field = value
        }

    var target: Target? = null
        set(value) {
            checkMethodSet()
            if (field != null) {
                throw SyntaxException("Target already set: $field")
            }
            if (value is Target.TargetQuery) {
                if (value.query.method != Query.Method.SELECT) {
                    throw SyntaxException("Only SELECT queries can be nested")
                }
            }
            if (value is Target.TargetField) {
                checkJsonTargetIsValid(value.value)
            }
            field = value
        }

    var describeProjection: String? = null
        set(value) {
            checkMethodSet()
            if (field != null) {
                throw SyntaxException("Projection already set: $field")
            }
            checkJsonPathIsValid(value!!, "DESCRIBE")
            field = value
        }

    var selectProjection: SelectProjection? = null
        set(value) {
            checkMethodSet()
            if (field != null) {
                throw SyntaxException("Projection already set: $field")
            }
            checkMath()
            when (value) {
                is SelectProjection.SingleField -> checkJsonPathIsValid(value.field, "SELECT")
                is SelectProjection.MultipleFields -> value.fields.forEach { checkJsonPathIsValid(it.first, "SELECT") }
            }

            field = value
        }

    var where: Where? = null
        set(value) {
            checkMethod("WHERE", Query.Method.SELECT, Query.Method.DESCRIBE)
            if (value!!.projection is WhereProjection.Field) {
                checkJsonPathIsValid((value.projection as WhereProjection.Field).value, "WHERE")
            }
            field = value
        }

    var searchOperator: Operator? = null
        set(value) {
            // TODO what's the correct error? (this only valid in search (unless the user malforms a where in select?)
            if (field != null) {
                throw SyntaxException("Search operator already set: $field")
            }
            field = value
        }

    var targetRange: SearchQuery.TargetRange? = null
        set(value) {
            checkMethod(value!!.name, Query.Method.SEARCH)
            if (field != null) {
                throw SyntaxException("Search target range already set: $field")
            }
            field = value
        }

    var searchValue: Value<*>? = null
        set(value) {
            // TODO what's the correct error? (this only valid in search (unless the user malforms a where in select?)
            if (field != null) {
                throw SyntaxException("Search value already set: $field")
            }
            field = value
        }

    var limit: Int? = null
        set(value) {
            checkMethod("LIMIT", Query.Method.SELECT, Query.Method.DESCRIBE)
            if (field != null) {
                throw SyntaxException("Limit already set: $field")
            }
            field = value
        }

    var offset: Int? = null
        set(value) {
            checkMethod("OFFSET", Query.Method.SELECT, Query.Method.DESCRIBE)
            if (field != null) {
                throw SyntaxException("Offset already set: $field")
            }
            field = value
        }

    var orderBy: ElementFieldProjection? = null
        set(value) {
            checkMethod("ORDER BY", Query.Method.SELECT)
            if (field != null) {
                throw SyntaxException("Order already set: $field")
            }
            if (value is ElementFieldProjection.Field) {
                checkJsonPathIsValid(value.value, "ORDER BY")
            }
            checkMath()
            field = value
        }

    var isDistinct: Boolean? = null
        set(value) {
            checkMethodSet()
            if (selectProjection is SelectProjection.Math) {
                throw SyntaxException("Can not use DISTINCT and MIN, MAX, SUM or COUNT together")
            }
            checkMath()
            field = value
        }

    var isCaseSensitive: Boolean? = null
        set(value) {
            checkMethodSet()
            if (method == Query.Method.SELECT || method == Query.Method.DESCRIBE) {
                if (where == null) {
                    throw SyntaxException("In SELECT or DESCRIBE: CASE SENSITIVE is only allowed after WHERE")
                }
            }
            field = value
        }

    var isWithValues: Boolean? = null
        set(value) {
            checkMethod("WITH VALUES", Query.Method.SEARCH)
            checkMath()
            field = value
        }

    var isWithKeys: Boolean? = null
        set(value) {
            checkMethod("WITH KEYS", Query.Method.DESCRIBE)
            field = value
        }

    var isByElement: Boolean? = null
        set(value) {
            checkMethod("BY ELEMENT", Query.Method.SELECT)
            field = value
        }

    var isAsJson: Boolean? = null
        set(value) {
            checkIsFalse("AS JSON", "KEYS", isOnlyPrintKeys)
            checkIsFalse("AS JSON", "VALUES", isOnlyPrintValues)
            checkMethod("AS JSON", Query.Method.SELECT)
            checkMath()
            field = value
        }

    var isPrettyPrinted: Boolean? = null
        set(value) {
            checkMethod("PRETTY", Query.Method.SELECT, Query.Method.DESCRIBE)
            if (isAsJson != true) {
                throw SyntaxException("In SELECT: PRETTY is only allowed after AS JSON")
            }
            field = value
        }

    var isOnlyPrintKeys: Boolean? = null
        set(value) {
            checkIsFalse("KEYS", "AS JSON", isAsJson)
            checkIsFalse("KEYS", "VALUES", isOnlyPrintValues)
            checkMethod("KEYS", Query.Method.SELECT)
            checkMath()
            field = value
        }

    var isOnlyPrintValues: Boolean? = null
        set(value) {
            checkIsFalse("VALUES", "AS JSON", isAsJson)
            checkIsFalse("VALUES", "KEYS", isOnlyPrintKeys)
            checkMethod("VALUES", Query.Method.SELECT)
            checkMath()
            field = value
        }

    var isOrderByDesc: Boolean? = null
        set(value) {
            checkMethod("VALUES", Query.Method.SELECT)
            if (orderBy == null) {
                throw SyntaxException("DESC is only allowed after ORDER BY")
            }
            field = value
        }

    fun build(): Query {
        if (method == null) {
            throw SyntaxException("No SELECT, DESCRIBE or SEARCH method found")
        }
        if (target == null) {
            throw SyntaxException("No target found")
        }

        val flags = Query.Flags(
                isDistinct = (isDistinct == true),
                isCaseSensitive = (isCaseSensitive == true),
                isAsJson = (isAsJson == true),
                isByElement = (isByElement == true),
                isOnlyPrintKeys = (isOnlyPrintKeys == true),
                isOnlyPrintValues = (isOnlyPrintValues == true),
                isPrettyPrinted = (isPrettyPrinted == true),
                isWithKeys = (isWithKeys == true),
                isWithValues = (isWithValues == true),
                isOrderByDesc = (isOrderByDesc == true)
        )

        var searchQuery: SearchQuery? = null
        var selectQuery: SelectQuery? = null
        var describeQuery: DescribeQuery? = null

        when (method!!) {
            Query.Method.SELECT -> {
                selectQuery = SelectQuery(selectProjection, limit, offset, orderBy)
            }
            Query.Method.DESCRIBE -> {
                describeQuery = DescribeQuery(describeProjection, limit, offset)
            }
            Query.Method.SEARCH -> {
                if (targetRange == null) {
                    throw SyntaxException("No target range found")
                }
                if (searchOperator == null) {
                    throw SyntaxException("No operator found")
                }
                if (searchValue == null) {
                    throw SyntaxException("No value found")
                }
                searchQuery = SearchQuery(targetRange!!, searchOperator!!, searchValue!!)
            }
        }

        return Query(queryString, method!!, target!!, flags, where, searchQuery, selectQuery, describeQuery)
    }

    private fun checkMath() {
        if (selectProjection is SelectProjection.Math) {
            if (isDistinct == true) {
                throw SyntaxException("Can not use DISTINCT and MIN, MAX, SUM or COUNT together")
            }
            if (orderBy != null) {
                throw SyntaxException("Can not use ORDER BY and MIN, MAX, SUM or COUNT together")
            }
            if (isOnlyPrintKeys != null) {
                throw SyntaxException("Can not use KEYS and MIN, MAX, SUM or COUNT together")
            }
            if (isOnlyPrintValues != null) {
                throw SyntaxException("Can not use VALUES and MIN, MAX, SUM or COUNT together")
            }
            if (isAsJson != null) {
                throw SyntaxException("Can not use VALUES and MIN, MAX, SUM or COUNT together")
            }
        }
    }

    private fun checkJsonTargetIsValid(path: String) {
        if (path.isEmpty()) throw SyntaxException("Json target is empty", SyntaxException.ExtraInfo.JSON_TARGET)
        if (path == ".") return
        if (path.endsWith(".")) throw SyntaxException("Json target ends with .", SyntaxException.ExtraInfo.JSON_TARGET)
        if (path.toSegments().any { it.isEmpty() }) throw SyntaxException("Json target contains blank segments", SyntaxException.ExtraInfo.JSON_TARGET)
    }

    private fun checkJsonPathIsValid(path: String, type: String) {
        if (path.isEmpty()) throw SyntaxException("Json path is empty", SyntaxException.ExtraInfo.JSON_PATH)
        if (path.startsWith(".")) throw SyntaxException("Json path for $type starts with .", SyntaxException.ExtraInfo.JSON_PATH)
        if (path.endsWith(".")) throw SyntaxException("Json path for $type ends with .", SyntaxException.ExtraInfo.JSON_PATH)
        if (path.toSegments().any { it.isEmpty() }) throw SyntaxException("Json path for $type contains blank segments", SyntaxException.ExtraInfo.JSON_PATH)
    }

    private fun checkIsFalse(field: String, flag: String, flagValue: Boolean?) {
        if (flagValue == true) {
            throw SyntaxException("$field can not be used with $flag")
        }
    }

    private fun checkMethod(keyword: String, vararg allowedMethods: Query.Method) {
        checkMethodSet()
        if (!allowedMethods.contains(method)) {
            throw SyntaxException("$keyword only works with ${allowedMethods.joinStringOr()}")
        }
    }

    private fun checkMethodSet() {
        if (method == null) {
            throw SyntaxException("Method (SELECT, DESCRIBE or SEARCH) must be first")
        }
    }

    private fun checkMethodNotSet() {
        if (method != null) {
            throw SyntaxException("Method can only be set once, already set to $method")
        }
    }

}