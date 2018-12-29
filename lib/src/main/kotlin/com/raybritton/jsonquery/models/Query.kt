package com.raybritton.jsonquery.models

import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.parsing.tokens.Token
import com.raybritton.jsonquery.parsing.tokens.isKeyword

internal data class Query(
        val originalString: String,
        val method: Method,
        val target: Target,
        val flags: Flags,
        val where: Where?,
        val search: SearchQuery? = null,
        val select: SelectQuery? = null,
        val describe: DescribeQuery? = null
) {

    enum class Method {
        SELECT, DESCRIBE, SEARCH
    }

    data class Flags(
            val isCaseSensitive: Boolean = false,
            val isDistinct: Boolean = false,
            val isWithValues: Boolean = false,
            val isByElement: Boolean = false,
            val isWithKeys: Boolean = false,
            val isAsJson: Boolean = false,
            val isPrettyPrinted: Boolean = false,
            val isOnlyPrintKeys: Boolean = false,
            val isOnlyPrintValues: Boolean = false,
            val isOrderByDesc: Boolean = false
    )

    override fun toString(): String {
        fun StringBuilder.appendKey(keyword: Keyword) = append(keyword.name)

        val builder = StringBuilder()
        when (method) {
            Method.SEARCH -> {
                builder.appendKey(Keyword.SEARCH).append(' ')
                if (flags.isDistinct) builder.appendKey(Keyword.DISTINCT).append(' ')
                builder.append(target).append(' ')
                        .appendKey(Keyword.FOR).append(' ')
                        .append(search!!.operator).append(' ')
                if (search.value is Value.ValueQuery) {
                    builder.append('(')
                }
                builder.append(search.value)
                if (search.value is Value.ValueQuery) {
                    builder.append(')')
                }
                if (flags.isCaseSensitive) builder.append(' ').appendKey(Keyword.CASE).append(' ').appendKey(Keyword.SENSITIVE)
                if (flags.isWithValues) builder.append(' ').appendKey(Keyword.WITH).append(' ').appendKey(Keyword.VALUES)
            }
            Method.SELECT -> {
                builder.appendKey(Keyword.SELECT).append(' ')
                if (flags.isDistinct) builder.appendKey(Keyword.DISTINCT).append(' ')
                builder.append(select!!.projection).append(' ')
                        .appendKey(Keyword.FROM).append(' ')
                        .append(target).append(' ')
                if (flags.isByElement) builder.appendKey(Keyword.BY).append(' ').appendKey(Keyword.ELEMENT).append(' ')
                if (where != null) builder.append(where)
                if (flags.isCaseSensitive) builder.append(' ').appendKey(Keyword.CASE).append(' ').appendKey(Keyword.SENSITIVE)
                if (select.limit != null) builder.append(' ').appendKey(Keyword.LIMIT).append(' ').append(select.limit)
                if (select.offset != null) builder.append(' ').appendKey(Keyword.OFFSET).append(' ').append(select.offset)
                if (flags.isWithKeys) builder.append(' ').appendKey(Keyword.WITH).append(' ').appendKey(Keyword.KEYS)
                if (flags.isAsJson) builder.append(' ').appendKey(Keyword.AS).append(' ').appendKey(Keyword.JSON)
                if (flags.isPrettyPrinted) builder.append(' ').appendKey(Keyword.PRETTY)

            }
            Method.DESCRIBE -> {
                builder.appendKey(Keyword.DESCRIBE).append(' ')
                if (flags.isDistinct) builder.appendKey(Keyword.DISTINCT).append(' ')
                builder.append(describe!!.projection).append(' ')
                        .appendKey(Keyword.FROM).append(' ')
                        .append(target).append(' ')
                if (where != null) builder.append(where)
                if (flags.isCaseSensitive) builder.append(' ').append(' ').appendKey(Keyword.CASE).append(' ').appendKey(Keyword.SENSITIVE)
                if (describe.limit != null) builder.append(' ').appendKey(Keyword.LIMIT).append(' ').append(describe.limit)
                if (describe.offset != null) builder.append(' ').appendKey(Keyword.OFFSET).append(' ').append(describe.offset)
                if (flags.isPrettyPrinted) builder.append(' ').appendKey(Keyword.PRETTY)
            }
        }
        return builder.toString()
    }
}

internal data class SearchQuery(val targetRange: TargetRange, val operator: Operator, val value: Value<*>) {
    enum class TargetRange {
        ANY, KEY, VALUE
    }
}

internal data class SelectQuery(val projection: SelectProjection?, val limit: Int?, val offset: Int?, val orderBy: ElementFieldProjection?)

internal data class DescribeQuery(val projection: String?, val limit: Int?, val offset: Int?)

internal data class Where(val projection: WhereProjection, val operator: Operator, val value: Value<*>)

internal sealed class Value<T>(val value: T) {
    class ValueNumber(value: Double) : Value<Double>(value)
    class ValueString(value: String) : Value<String>(value)
    class ValueBoolean(value: Boolean) : Value<Boolean>(value)
    class ValueQuery(value: Query) : Value<Query>(value)
    object ValueNull : Value<Unit>(Unit)

    companion object {
        fun build(any: Token<*>?): Value<*>? {
            return when {
                any.isKeyword(Keyword.NULL) -> ValueNull
                any is Token.STRING -> Value.ValueString(any.value)
                any is Token.NUMBER -> Value.ValueNumber(any.value)
                any.isKeyword(Keyword.TRUE, Keyword.FALSE) -> Value.ValueBoolean(any?.value == Keyword.TRUE.name)
                else -> null
            }
        }
    }
}

internal sealed class Target {
    class TargetField(val value: String) : Target()
    class TargetQuery(val query: Query) : Target()
}

internal sealed class ElementFieldProjection {
    class Field(val value: String) : ElementFieldProjection()
    object Element : ElementFieldProjection()

    companion object {
        fun build(any: Token<*>?): ElementFieldProjection? {
            return when {
                any is Token.STRING -> Field(any.value)
                any.isKeyword(Keyword.ELEMENT) -> Element
                else -> null
            }
        }
    }
}

internal sealed class WhereProjection {
    class Field(val value: String) : WhereProjection()
    object Element : WhereProjection()
    class Math(val expr: Keyword, val field: ElementFieldProjection) : WhereProjection()

    companion object {
        fun build(any: Token<*>?): WhereProjection? {
            return when {
                any is Token.STRING -> WhereProjection.Field(any.value)
                any.isKeyword(Keyword.ELEMENT) -> WhereProjection.Element
                else -> null
            }
        }
    }
}

internal sealed class SelectProjection {
    object All : SelectProjection()
    class SingleField(val field: String) : SelectProjection()
    class MultipleFields(val fields: List<String>) : SelectProjection()
    class Math(val expr: Keyword, val field: ElementFieldProjection) : SelectProjection()
}