package com.raybritton.jsonquery.parsing

import com.raybritton.jsonquery.SyntaxException
import com.raybritton.jsonquery.models.Query

@Throws(SyntaxException::class)
internal fun List<Token>.buildQuery(): Query {
    var tokenIdx = 0
    val builder = Query.Builder()

    while (tokenIdx < size) {
        val token = this[tokenIdx]

        when (token.type) {
            Token.Type.KEYWORD -> {
                when (token.value) {
                    "SELECT" -> {
                        builder.checkMethodNotSet()
                        tokenIdx += parseSelect(this.subList(tokenIdx, size).toMutableList(), builder)
                    }
                    "DESCRIBE" -> {
                        builder.checkMethodNotSet()
                        tokenIdx += parseDescribe(this.subList(tokenIdx, size).toMutableList(), builder)
                    }
                    "SEARCH" -> {
                        builder.checkMethodNotSet()
                        tokenIdx += parseSearch(this.subList(tokenIdx, size).toMutableList(), builder)
                    }
                    "WHERE" -> {
                        builder.allowedMethods("AS JSON", Query.Method.SELECT, Query.Method.DESCRIBE)
                        tokenIdx += parseWhere(this.subList(tokenIdx, size).toMutableList(), builder)
                    }
                    "LIMIT" -> {
                        builder.allowedMethods("LIMIT", Query.Method.SELECT, Query.Method.DESCRIBE)
                        builder.limit = integerReader("LIMIT", this.getOrNull(tokenIdx + 1))
                        tokenIdx++
                    }
                    "OFFSET" -> {
                        builder.allowedMethods("OFFSET", Query.Method.SELECT, Query.Method.DESCRIBE)
                        builder.offset = integerReader("OFFSET", this.getOrNull(tokenIdx + 1))
                        tokenIdx++
                    }
                    "ASJSON" -> {
                        builder.allowedMethods("AS JSON", Query.Method.SELECT)
                        builder.checkTargetExtras("AS JSON", Query.TargetExtra.SPECIFIC)
                        builder.asJson = true
                    }
                    "PRETTY" -> {
                        builder.allowedMethods("PRETTY", Query.Method.SELECT, Query.Method.DESCRIBE)
                        if (builder.method == Query.Method.SELECT && !builder.asJson) {
                            throw SyntaxException("PRETTY only supported with AS JSON on SELECT")
                        }
                        builder.pretty = true
                    }
                    "WITHKEYS" -> {
                        builder.allowedMethods("WITH KEYS", Query.Method.SELECT)
                        builder.checkTargetExtras("WITH KEYS", Query.TargetExtra.SPECIFIC)
                        builder.withKeys = true
                    }
                    "ORDERBY" -> {
                        builder.allowedMethods("ORDER BY", Query.Method.SELECT)
                        tokenIdx += parseOrderBy(this.subList(tokenIdx, size), builder)
                    }
                    else -> throw SyntaxException(token, "KEYWORD")
                }
            }
            else -> throw SyntaxException(token, "KEYWORD")
        }

        tokenIdx++
    }

    return builder.build()
}

private fun parseSelect(list: MutableList<Token>, builder: Query.Builder): Int {
    val keysOrValues = { hasFrom: Boolean, fromIdx: Int, extra: Query.TargetExtra ->
        if (hasFrom) {
            if (fromIdx == 2) {
                builder.targetExtra = extra
                builder.target = list[3].value
            } else {
                throw SyntaxException(list[2], "FROM")
            }
        } else {
            throw SyntaxException("$extra requires FROM")
        }
    }

    val selectSupportedValues = "([KEYS | VALUES | column | columns | mathExpr FROM] jsonPath) "

    var returnValue = 0
    builder.method = Query.Method.SELECT
    if (list.getOrNull(1)?.type == Token.Type.KEYWORD && list.getOrNull(1)?.value == "DISTINCT") {
        list.removeAt(1)
        builder.distinct = true
        returnValue++
    }

    if (list.size < 2) {
        throw SyntaxException("SELECT must be followed by $selectSupportedValues")
    }

    val fromIdx = list.indexOfFirst { it.type == Token.Type.KEYWORD && it.value == "FROM" }
    val hasFrom = list[fromIdx + 1].type == Token.Type.STRING

    when (list[1].type) {
        Token.Type.KEYWORD -> {
            when (list[1].value) {
                "KEYS", "VALUES" -> {
                    keysOrValues(hasFrom, fromIdx, Query.TargetExtra.valueOf(list[1].value))
                    returnValue += 3
                }
                "MIN","COUNT","SUM","MAX" -> {
                    if (hasFrom) {
                        returnValue += parseMath(list.subList(1, list.size), builder)
                        builder.target = list[fromIdx + 1].value
                        returnValue += 2
                    } else {
                        throw SyntaxException("${list[1].type} requires FROM")
                    }
                }
                else -> throw SyntaxException(list[1], selectSupportedValues)
            }
        }
        Token.Type.PUNCTUATION, Token.Type.STRING -> {
            returnValue += parseColumns(fromIdx, hasFrom, selectSupportedValues, list.subList(1, list.size), builder)
        }
        else -> throw SyntaxException(list[1], selectSupportedValues)
    }
    return returnValue
}

private fun parseDescribe(list: MutableList<Token>, builder: Query.Builder): Int {
    val describeSupportedValues = "([column | columns FROM] jsonPath) "

    var returnValue = 0
    builder.method = Query.Method.DESCRIBE
    if (list.getOrNull(1)?.type == Token.Type.KEYWORD && list.getOrNull(1)?.value == "DISTINCT") {
        list.removeAt(1)
        builder.distinct = true
        returnValue++
    }

    if (list.size < 2) {
        throw SyntaxException("DESCRIBE must be followed by $describeSupportedValues")
    }

    val fromIdx = list.indexOfFirst { it.type == Token.Type.KEYWORD && it.value == "FROM" }
    val hasFrom = list[fromIdx + 1].type == Token.Type.STRING

    when (list[1].type) {
        Token.Type.PUNCTUATION, Token.Type.STRING -> {
            returnValue += parseColumns(fromIdx, hasFrom, describeSupportedValues, list.subList(1, list.size), builder)
        }
        else -> throw SyntaxException(list[1], describeSupportedValues)
    }

    return returnValue
}

private fun parseColumns(fromIdx: Int, hasFrom: Boolean, supportedValuesMessage: String, list: List<Token>, builder: Query.Builder): Int {
    var returnValue = 0
    when (list[0].type) {
        Token.Type.PUNCTUATION -> {
            if (hasFrom && list[0].value == "(") {
                builder.targetExtra = Query.TargetExtra.SPECIFIC
                val close = list.indexOfFirst { it.type == Token.Type.PUNCTUATION && it.value == ")" }
                if (close == -1) {
                    throw SyntaxException(list[0], "closing parenthesis")
                } else {
                    val keys = (1 until close).mapNotNull {
                        val token = list[it]
                        when (token.type) {
                            Token.Type.STRING -> return@mapNotNull token.value
                            Token.Type.PUNCTUATION -> {
                                if (token.value == ",") {
                                    return@mapNotNull null
                                } else {
                                    throw SyntaxException(token, "column or comma")
                                }
                            }
                            else -> throw SyntaxException(token, "column or comma")
                        }
                    }
                    builder.targetKeys = keys
                    builder.target = list[fromIdx].value
                    returnValue += (fromIdx + 1)
                }
            } else {
                throw SyntaxException(list[0], supportedValuesMessage)
            }
        }
        Token.Type.STRING -> {
            if (hasFrom) {
                builder.targetExtra = Query.TargetExtra.SPECIFIC
                builder.targetKeys = listOf(list[0].value)
                builder.target = list[2].value
                returnValue += 3
            } else {
                builder.target = list[0].value
                returnValue += 1
            }
        }
    }
    return returnValue
}

private fun parseSearch(list: List<Token>, builder: Query.Builder): Int {
    builder.method = Query.Method.SEARCH
    if (list.size == 2 || list[1].type != Token.Type.STRING) {
        throw SyntaxException("SEARCH must be followed by (column)")
    }
    builder.target = list[1].value
    if (list.size == 3 || list[2].type != Token.Type.KEYWORD || list[2].value != "FOR") {
        throw SyntaxException(list[2], "FOR")
    }
    if (list.size == 4) {
        if (list[3].type == Token.Type.STRING) {
            builder.targetExtra = Query.TargetExtra.BOTH
            builder.targetKeys = listOf(list[3].value)
            return 3
        } else {
            throw SyntaxException(list[3], "(KEY | VALUE) or search term (must be a string)")
        }
    } else {
        if (list[3].type == Token.Type.KEYWORD && list[4].type == Token.Type.STRING && (list[3].value == "KEY" || list[3].value == "VALUE")) {
            builder.targetExtra = Query.TargetExtra.valueOf(list[3].value)
            builder.targetKeys = listOf(list[4].value)
            return 4
        } else {
            throw SyntaxException(list[3], "(KEY | VALUE) or search term (must be a string)")
        }
    }
}

private fun parseMath(list: List<Token>, builder: Query.Builder): Int {
    if (builder.distinct) {
        throw SyntaxException("${list[0].value} does not support DISTINCT")
    }
    if (list.size >=4 &&
            list[1].type == Token.Type.PUNCTUATION && list[1].value == "(" &&
            list[3].type == Token.Type.PUNCTUATION && list[3].value == ")" &&
            ((list[2].type == Token.Type.KEYWORD && list[2].value == "ELEMENT") ||
            (list[2].type == Token.Type.STRING))) {
        builder.targetExtra = Query.TargetExtra.valueOf(list[0].value)
        builder.targetKeys = listOf(list[2].value)
        return 4
    } else {
        throw SyntaxException("${list[0].value} must be followed by (ELEMENT | column)")
    }
}

private fun parseWhere(list: List<Token>, builder: Query.Builder): Int {
    val whereSupportedValues = "WHERE must be followed by ((column | ELEMENT) operator value)"

    val whereBuilder = Query.Where.Builder()

    if (list.size < 4) {
        throw SyntaxException(whereSupportedValues)
    } else {
        if ((list[1].type == Token.Type.KEYWORD && list[1].value == "ELEMENT") || list[1].type == Token.Type.STRING) {
            whereBuilder.field = list[1].value
        } else {
            throw SyntaxException(list[1], "(column | ELEMENT)")
        }
        if (list[2].type == Token.Type.PUNCTUATION) {
            try {
                whereBuilder.operator = Query.Where.getOperatorBySymbol(list[2].value)
            } catch (e: IllegalArgumentException) {
                throw SyntaxException(list[2], "Operator must be (== | != | < | > | # | !#)")
            }
        }
        when (list[3].type) {
            Token.Type.STRING, Token.Type.NUMBER -> whereBuilder.compare = list[3].value
            else -> throw SyntaxException(list[3], "string or number")
        }
    }
    builder.where = whereBuilder.build()
    return 3
}

private fun parseOrderBy(list: List<Token>, builder: Query.Builder): Int {
    if (list[1].type == Token.Type.STRING) {
        builder.order = list[1].value
        if (list.size > 2 && list[2].type == Token.Type.KEYWORD && list[2].value == "DESC") {
            builder.desc = true
            return 2
        } else {
            return 1
        }
    } else {
        throw SyntaxException("ORDER BY must be followed by (string [DESC])")
    }
}

private fun integerReader(field: String, nextToken: Token?): Int {
    if (nextToken == null || nextToken.type != Token.Type.NUMBER) {
        throw SyntaxException("$field must be followed an integer")
    }
    if (nextToken.value.toDouble() % 1.0 != 0.0) throw SyntaxException("$field number must be an integer")
    return nextToken.value.toInt()
}