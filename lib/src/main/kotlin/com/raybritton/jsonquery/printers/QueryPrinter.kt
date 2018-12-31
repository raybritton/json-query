package com.raybritton.jsonquery.printers

import com.raybritton.jsonquery.ext.wrap
import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.parsing.tokens.Keyword

internal object QueryPrinter {
    fun print(query: Query): String {
        fun StringBuilder.appendKey(keyword: Keyword) = append(' ').append(keyword.name)

        val builder = StringBuilder()
        when (query.method) {
            Query.Method.SEARCH -> {
                builder.appendKey(Keyword.SEARCH).append(' ')
                if (query.flags.isDistinct) builder.appendKey(Keyword.DISTINCT).append(' ')
                builder.append(printTarget(query.target)).append(' ')
                        .appendKey(Keyword.FOR).append(' ')
                        .append(query.search!!.operator.symbol).append(' ')
                if (query.search.value is Value.ValueQuery) {
                    builder.append('(')
                }
                builder.append(query.search.value)
                if (query.search.value is Value.ValueQuery) {
                    builder.append(')')
                }
                if (query.flags.isCaseSensitive) builder.appendKey(Keyword.CASE).appendKey(Keyword.SENSITIVE)
                if (query.flags.isWithValues) builder.appendKey(Keyword.WITH).appendKey(Keyword.VALUES)
            }
            Query.Method.SELECT -> {
                builder.appendKey(Keyword.SELECT).append(' ')
                if (query.flags.isDistinct) builder.appendKey(Keyword.DISTINCT).append(' ')
                if (query.select!!.projection != null && query.select.projection !is SelectProjection.All) {
                    builder.append(printSelectProjection(query.select.projection!!))
                            .appendKey(Keyword.FROM).append(' ')
                }
                builder.append(printTarget(query.target)).append(' ')
                if (query.flags.isByElement) builder.appendKey(Keyword.BY).appendKey(Keyword.ELEMENT).append(' ')
                if (query.where != null) builder.append(printWhere(query.where))
                if (query.flags.isCaseSensitive) builder.appendKey(Keyword.CASE).appendKey(Keyword.SENSITIVE)
                if (query.select.limit != null) builder.appendKey(Keyword.LIMIT).append(' ').append(query.select.limit)
                if (query.select.offset != null) builder.appendKey(Keyword.OFFSET).append(' ').append(query.select.offset)
                if (query.select.orderBy != null) builder.appendKey(Keyword.ORDER).appendKey(Keyword.BY).append(' ').append(query.select.orderBy)
                if (query.flags.isAsJson) builder.appendKey(Keyword.AS).appendKey(Keyword.JSON)
                if (query.flags.isPrettyPrinted) builder.appendKey(Keyword.PRETTY)

            }
            Query.Method.DESCRIBE -> {
                builder.appendKey(Keyword.DESCRIBE).append(' ')
                if (query.flags.isDistinct) builder.appendKey(Keyword.DISTINCT).append(' ')
                if (query.describe!!.projection != null) {
                    builder.append(query.describe.projection.wrap())
                            .appendKey(Keyword.FROM).append(' ')
                }
                builder.append(printTarget(query.target)).append(' ')
                if (query.where != null) builder.append(printWhere(query.where))
                if (query.flags.isCaseSensitive) builder.append(' ').appendKey(Keyword.CASE).appendKey(Keyword.SENSITIVE)
                if (query.describe.limit != null) builder.appendKey(Keyword.LIMIT).append(' ').append(query.describe.limit)
                if (query.describe.offset != null) builder.appendKey(Keyword.OFFSET).append(' ').append(query.describe.offset)
                if (query.flags.isPrettyPrinted) builder.appendKey(Keyword.PRETTY)
            }
        }
        return builder.toString()
    }

    private fun printWhere(where: Where): String {
        val builder = StringBuilder()

        builder.append(Keyword.WHERE.name).append(' ')

        when (where.projection) {
            is WhereProjection.Field -> builder.append(where.projection.value.wrap())
            WhereProjection.Element -> builder.append(Keyword.ELEMENT.name)
            is WhereProjection.Math -> {
                builder.append(where.projection.expr)
                builder.append('(')
                when (where.projection.field) {
                    is ElementFieldProjection.Field -> builder.append(where.projection.field.value.wrap())
                    ElementFieldProjection.Element -> builder.append(Keyword.ELEMENT.name)
                }
                builder.append(')')
            }
        }

        builder.append(' ')

        builder.append(where.operator.symbol)

        builder.append(' ')

        builder.append(where.value)

        return builder.toString()
    }

    private fun printSelectProjection(selectProjection: SelectProjection): String {
        val builder = StringBuilder()

        when (selectProjection) {
            is SelectProjection.SingleField -> builder.append(selectProjection.field)
            is SelectProjection.MultipleFields -> builder.append(selectProjection.fields.joinToString(", ", prefix = "(", postfix = ")", transform = { it.wrap() }))
            is SelectProjection.Math -> {
                builder.append(selectProjection.expr)
                builder.append('(')
                when (selectProjection.field) {
                    is ElementFieldProjection.Field -> builder.append(selectProjection.field.value.wrap())
                    ElementFieldProjection.Element -> builder.append(Keyword.ELEMENT.name)
                }
                builder.append(')')
            }
            else -> {
            }
        }

        return builder.toString()
    }

    private fun printTarget(target: Target): String {
        return when (target) {
            is Target.TargetField -> target.value.wrap()
            is Target.TargetQuery -> "(" + target.query.originalString + ")"
        }
    }
}