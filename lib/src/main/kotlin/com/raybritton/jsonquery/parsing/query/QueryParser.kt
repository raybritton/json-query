package com.raybritton.jsonquery.parsing.query

import com.raybritton.jsonquery.SyntaxException
import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.tokens.*
import java.util.*

/**
 * Turns a list of tokens into a query
 *
 * @see Token
 * @see Query
 */

internal fun List<Token<*>>.buildQuery(queryString: String): Query {
    return ArrayDeque(this).buildQueryUntil(queryString)
}

@Throws(SyntaxException::class)
private fun ArrayDeque<Token<*>>.buildQueryUntil(queryString: String, stop: Token<*>? = null): Query {
    val builder = QueryBuilder(queryString)

    while (this.isNotEmpty()) {
        val token = this.pop()

        stop?.let {
            if (token::class.java == it::class.java && token.value == it.value) {
                return builder.build()
            }
        }

        when (token) {
            is Token.KEYWORD -> {
                when (token.value) {
                    Keyword.SELECT -> {
                        parseSelect(this, builder)
                    }
                    Keyword.DESCRIBE -> {
                        parseDescribe(this, builder)
                    }
                    Keyword.SEARCH -> {
                        parseSearch(this, builder)
                    }
                    Keyword.WHERE -> {
                        parseWhere(this, builder)
                    }
                    Keyword.LIMIT -> {
                        builder.limit = pollFirst().readInt(Keyword.LIMIT)
                    }
                    Keyword.OFFSET -> {
                        builder.offset = pollFirst().readInt(Keyword.OFFSET)
                    }
                    Keyword.BY -> {
                        checkMultipartFlag(token, pollFirst(), Keyword.ELEMENT)
                        builder.isByElement = true
                    }
                    Keyword.AS -> {
                        checkMultipartFlag(token, pollFirst(), Keyword.JSON)
                        builder.isAsJson = true
                    }
                    Keyword.PRETTY -> {
                        builder.isPrettyPrinted = true
                    }
                    Keyword.WITH -> {
                        val nextToken = pollFirst()
                        if (nextToken.isKeyword(Keyword.VALUES)) {
                            builder.isWithValues = true
                        } else if (nextToken.isKeyword(Keyword.KEYS)) {
                            builder.isWithKeys = true
                        } else {
                            throw SyntaxException.throwNullable(nextToken, "VALUES or KEYS")
                        }
                    }
                    Keyword.ORDER -> {
                        checkMultipartFlag(token, pollFirst(), Keyword.BY)
                        parseOrderBy(this, builder)
                    }
                    Keyword.CASE -> {
                        checkMultipartFlag(token, pollFirst(), Keyword.SENSITIVE)
                        @Suppress("NON_EXHAUSTIVE_WHEN") //not needed as SEARCH doesn't need checking
                        when (builder.method) {
                            Query.Method.DESCRIBE, Query.Method.SELECT -> {
                                if (builder.where == null) {
                                    throw SyntaxException(token, " nothing: CASE SENSITIVE only works with WHERE")
                                }
                            }
                        }
                        builder.isCaseSensitive = true
                    }
                    else -> {
                        throw SyntaxException(token, "KEYWORD")
                    }
                }
            }
            else -> {
                throw SyntaxException(token, "KEYWORD")
            }
        }
    }

    return builder.build()
}

private fun checkMultipartFlag(token: Token<*>, nextToken: Token<*>?, nextKeyword: Keyword) {
    if (nextToken.isKeyword(nextKeyword)) {
        return
    } else {
        if (nextToken == null) {
            throw SyntaxException("${token.value} must be followed by $nextKeyword")
        } else {
            throw SyntaxException(nextToken, nextKeyword.name)
        }
    }
}

private fun parseSelect(list: ArrayDeque<Token<*>>, builder: QueryBuilder) {
    builder.method = Query.Method.SELECT

    val handleTarget = { token: Token<*>? ->
        when {
            token.isPunctuation('(') -> {
                val result = list.buildQueryUntil(builder.queryString, Token.PUNCTUATION(')', 0))
                if (!result.flags.isAsJson) {
                    throw SyntaxException("Nested query starting at ${token!!.charIdx} is a target, it must use AS JSON")
                }
                Target.TargetQuery(result)
            }
            token is Token.STRING -> Target.TargetField(token.value)
            else -> SyntaxException.throwNullable(token, "json path or query (surrounded by parenthesis)")
        }
    }

    if (list.peek().isKeyword(Keyword.DISTINCT)) {
        list.poll()
        builder.isDistinct = true
    }

    val token = list.poll()

    when {
        token.isKeyword(Keyword.MIN, Keyword.MAX, Keyword.SUM, Keyword.COUNT) -> {
            val projection = parseMathField(list)
            builder.selectProjection = SelectProjection.Math((token as Token.KEYWORD).value, projection)
            list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "FROM")
            builder.target = handleTarget(list.poll())
        }
        token.isPunctuation('(') -> {
            if (list.peek().isKeyword(Keyword.SELECT)) {
                builder.target = handleTarget(token)
            } else {
                builder.selectProjection = parseMultipleFields(list)
                list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "FROM")
                builder.target = handleTarget(list.poll())
            }
        }
        token.isKeyword(Keyword.KEYS) -> {
            builder.isOnlyPrintKeys = true
            builder.selectProjection = SelectProjection.All
            list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "FROM")
            builder.target = handleTarget(list.poll())
        }
        token.isKeyword(Keyword.VALUES) -> {
            builder.isOnlyPrintValues = true
            builder.selectProjection = SelectProjection.All
            list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "FROM")
            builder.target = handleTarget(list.poll())
        }
        token is Token.STRING -> {
            if (list.peek().isKeyword(Keyword.AS)) {
                list.checkFirstElement({ it.isKeyword(Keyword.AS) }, "AS or FROM")
                if (list.peek().isKeyword(Keyword.JSON)) {
                    list.checkFirstElement({ it.isKeyword(Keyword.JSON) }, "JSON or alias name for field")
                    builder.isAsJson = true
                    builder.target = handleTarget(token)
                } else {
                    val projection = token.value

                    val nextToken = list.poll()
                    val newName = when (nextToken) {
                        is Token.STRING -> nextToken.value
                        else -> throw SyntaxException(nextToken, "alias name for field")
                    }
                    builder.selectProjection = SelectProjection.SingleField(projection, newName)
                    list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "FROM")
                    builder.target = handleTarget(list.poll())
                }
            } else if (list.peek().isKeyword(Keyword.FROM)) {
                builder.selectProjection = SelectProjection.SingleField(token.value, null)
                list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "AS or FROM")
                builder.target = handleTarget(list.poll())
            } else builder.target = handleTarget(token)
        }
        else -> SyntaxException.throwNullable(token, "json path or multiple json paths (surrounded by parenthesis) or KEYS or VALUES or query (surrounded by parenthesis)")
    }
}

private fun parseDescribe(list: ArrayDeque<Token<*>>, builder: QueryBuilder) {
    builder.method = Query.Method.DESCRIBE

    val handleTarget = { token: Token<*> ->
        when {
            token.isPunctuation('(') -> {
                val result = list.buildQueryUntil(builder.queryString, Token.PUNCTUATION(')', 0))
                if (!result.flags.isAsJson) {
                    throw SyntaxException("Nested query starting at ${token.charIdx} is a target, it must use AS JSON")
                }
                Target.TargetQuery(result)
            }
            token is Token.STRING -> Target.TargetField(token.value)
            else -> SyntaxException.throwNullable(token, "json path or query (surrounded by parenthesis)")
        }
    }

    val handleProjection = { token: Token<*> ->
        when (token) {
            is Token.STRING -> builder.describeProjection = token.value
            else -> SyntaxException.throwNullable(token, "json path")
        }
    }

    list.peek().let {
        when {
            it.isKeyword(Keyword.DISTINCT) -> {
                list.poll()
                builder.isDistinct = true
                handleProjection(it)
            }
            else -> {
                if (list.peek() is Token.STRING) {
                    val string = list.poll()
                    if (list.peek().isKeyword(Keyword.FROM)) {
                        handleProjection(string)
                        list.checkFirstElement({ it.isKeyword(Keyword.FROM) }, "FROM")
                        builder.target = handleTarget(list.pollFirst())
                    } else {
                        builder.target = handleTarget(string)
                    }
                } else if (list.peek().isPunctuation('(')) {
                    builder.target = handleTarget(list.pollFirst())
                }
            }
        }
    }




}

private fun parseMultipleFields(list: ArrayDeque<Token<*>>): SelectProjection.MultipleFields {
    val fields = mutableListOf<Pair<String, String?>>()
    var token = list.pollFirst()
    while (token != null && !token.isPunctuation(')')) {
        when {
            token is Token.STRING -> {
                if (list.peek().isKeyword(Keyword.AS)) {
                    list.checkFirstElement({ it.isKeyword(Keyword.AS) }, "AS")
                    if (list.peek() is Token.STRING) {
                        fields.add(token.value to (list.poll() as Token.STRING).value)
                    } else {
                        throw SyntaxException(list.poll(), "alias name for field")
                    }
                } else {
                    fields.add(token.value to null)
                }
            }
            token.isPunctuation(',') -> {
            }
            else -> SyntaxException.throwNullable(token, "json path or , or )")
        }
        token = list.pollFirst()
    }
    if (fields.isEmpty()) {
        throw SyntaxException("No fields found in target")
    }
    return SelectProjection.MultipleFields(fields)
}

private fun parseSearch(list: ArrayDeque<Token<*>>, builder: QueryBuilder) {
    builder.method = Query.Method.SEARCH

    val handleTarget = { token: Token<*> ->
        when {
            token.isPunctuation('(') -> {
                val result = list.buildQueryUntil(builder.queryString, Token.PUNCTUATION(')', 0))
                if (!result.flags.isAsJson) {
                    throw SyntaxException("Nested query starting at ${token.charIdx} is a target, it must use AS JSON")
                }
                builder.target = Target.TargetQuery(result)
            }
            token is Token.STRING -> builder.target = Target.TargetField(token.value)
            else -> SyntaxException.throwNullable(token, "json path or query (surrounded by parenthesis)")
        }
    }

    list.pollFirst().let {
        when {
            it.isKeyword(Keyword.DISTINCT) -> builder.isDistinct = true
            else -> handleTarget(it)
        }
    }

    if (builder.isDistinct == true) {
        handleTarget(list.pollFirst())
    }

    list.checkFirstElement({ it.isKeyword(Keyword.FOR) }, "FOR")

    list.pollFirst().let {
        when {
            it.isKeyword(Keyword.ANY) || it.isKeyword(Keyword.KEY) || it.isKeyword(Keyword.VALUE) ->
                builder.targetRange = SearchQuery.TargetRange.valueOf((it as Token.KEYWORD).value.name)
            else -> SyntaxException.throwNullable(it, "ANY or KEY or VALUE")
        }
    }

    list.pollFirst().let {
        if (it is Token.OPERATOR) {
            builder.searchOperator = it.value
        } else {
            SyntaxException.throwNullable(it, "operator")
        }
    }

    builder.searchValue = parseValue(list, builder)
}

private fun parseWhere(list: ArrayDeque<Token<*>>, builder: QueryBuilder) {
    val projection: WhereProjection
    val operator: Operator

    list.pollFirst().let {
        var field = WhereProjection.build(it)
        if (field == null) {
            if (it.isKeyword(Keyword.MIN, Keyword.MAX, Keyword.SUM, Keyword.COUNT)) {
                val mathProjection = parseMathField(list)
                field = WhereProjection.Math((it as Token.KEYWORD).value, mathProjection)
            } else {
                SyntaxException.throwNullable(it, "json path or ELEMENT or math expression")
            }
        }
        projection = field
    }

    list.pollFirst().let {
        when {
            it is Token.OPERATOR -> operator = it.value
            it.isKeyword(Keyword.IS) -> {
                if (list.peek().isKeyword(Keyword.NOT)) {
                    list.pollFirst()
                    operator = Operator.TypeNotEqual
                } else {
                    operator = Operator.TypeEqual
                }
            }
            else -> SyntaxException.throwNullable(it, "operator or IS")
        }
    }

    val value = parseValue(list, builder)

    builder.where = Where(projection, operator, value)
}

private fun parseOrderBy(list: ArrayDeque<Token<*>>, builder: QueryBuilder) {
    list.pollFirst().let {
        when {
            it is Token.STRING || it.isKeyword(Keyword.ELEMENT) -> builder.orderBy = ElementFieldProjection.build(it) ?: SyntaxException.throwNullable(it, "json path or ELEMENT")
            else -> SyntaxException.throwNullable(it, "json path or ELEMENT")
        }
    }
    if (list.peekFirst().isKeyword(Keyword.DESC)) {
        list.pop()
        builder.isOrderByDesc = true
    }
}

/**
 * Assumes math keyword has already been consumed
 */
private fun parseMathField(list: ArrayDeque<Token<*>>): ElementFieldProjection {
    val projection: ElementFieldProjection
    list.checkFirstElement({ it.isPunctuation('(') }, "beginning of math projection: (")

    list.pollFirst().let {
        projection = ElementFieldProjection.build(it) ?: throw SyntaxException(it, "json path or ELEMENT")
    }

    list.checkFirstElement({ it.isPunctuation(')') }, "end of math projection: (")

    return projection
}

private fun parseValue(list: ArrayDeque<Token<*>>, builder: QueryBuilder): Value<*> {
    val token = list.pollFirst()
    var compare = Value.build(token)
    if (compare == null) {
        if (token.isPunctuation('(')) {
            val query = list.buildQueryUntil(builder.queryString, Token.PUNCTUATION(')', 0))
            compare = Value.ValueQuery(query)
        } else {
            if (builder.searchOperator == Operator.TypeEqual || builder.searchOperator == Operator.TypeNotEqual) {
                SyntaxException.throwNullable(token, "STRING, NUMBER, OBJECT, BOOLEAN, ARRAY or NULL")
            } else {
                SyntaxException.throwNullable(token, "number or string or boolean or query (surrounded by parenthesis)")
            }
        }
    }
    return compare
}

private fun Token<*>?.readInt(field: Keyword): Int {
    if (this !is Token.NUMBER) {
        SyntaxException.throwNullable(this, "$field must be followed an integer")
    }
    if (!this.isInteger) throw SyntaxException(this, "$field number must be an integer")
    return this.asInteger()
}

private fun ArrayDeque<Token<*>>.checkFirstElement(predicate: (Token<*>) -> Boolean, expected: String) {
    val value = pollFirst()
    if (value == null || !predicate(value)) {
        SyntaxException.throwNullable(value, expected)
    }
}
