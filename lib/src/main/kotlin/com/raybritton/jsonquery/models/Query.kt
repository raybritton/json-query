package com.raybritton.jsonquery.models

import com.raybritton.jsonquery.ext.wrap
import com.raybritton.jsonquery.parsing.tokens.Keyword
import com.raybritton.jsonquery.parsing.tokens.Operator
import com.raybritton.jsonquery.parsing.tokens.Token
import com.raybritton.jsonquery.parsing.tokens.isKeyword
import com.raybritton.jsonquery.printers.QueryPrinter

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
            val isAsJson: Boolean = false,
            val isPrettyPrinted: Boolean = false,
            val isOnlyPrintKeys: Boolean = false,
            val isOnlyPrintValues: Boolean = false,
            val isOrderByDesc: Boolean = false
    )

    override fun toString() = QueryPrinter.print(this)
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

    override fun toString(): String {
        return when (this) {
            is ValueString -> value.wrap()
            is ValueNull -> "null"
            else -> value.toString()
        }
    }

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
    class TargetField(val value: String) : Target() {
        override fun toString() = value.wrap()
    }

    class TargetQuery(val query: Query) : Target() {
        override fun toString() = query.toString()
    }
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