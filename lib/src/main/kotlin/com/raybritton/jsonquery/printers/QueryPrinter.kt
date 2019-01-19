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
                builder.appendKey(Keyword.SEARCH)
                if (query.flags.isDistinct) builder.appendKey(Keyword.DISTINCT)
                builder.append(' ').append(printTarget(query.target)).append(' ')
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
                builder.appendKey(Keyword.SELECT)
                if (query.flags.isDistinct) builder.appendKey(Keyword.DISTINCT)
                if (query.select!!.projection != null && query.select.projection !is SelectProjection.All) {
                    builder.append(' ').append(printSelectProjection(query.select.projection!!))
                            .appendKey(Keyword.FROM)
                }
                builder.append(' ').append(printTarget(query.target))
                if (query.flags.isByElement) builder.appendKey(Keyword.BY).appendKey(Keyword.ELEMENT)
                if (query.where != null) builder.append(printWhere(query.where))
                if (query.flags.isCaseSensitive) builder.appendKey(Keyword.CASE).appendKey(Keyword.SENSITIVE)
                if (query.select.limit != null) builder.appendKey(Keyword.LIMIT).append(' ').append(query.select.limit)
                if (query.select.offset != null) builder.appendKey(Keyword.OFFSET).append(' ').append(query.select.offset)
                if (query.select.orderBy != null) builder.appendKey(Keyword.ORDER).appendKey(Keyword.BY).append(' ').append(query.select.orderBy)
                if (query.flags.isAsJson) builder.appendKey(Keyword.AS).appendKey(Keyword.JSON)
                if (query.flags.isPrettyPrinted) builder.appendKey(Keyword.PRETTY)

            }
            Query.Method.DESCRIBE -> {
                builder.appendKey(Keyword.DESCRIBE)
                if (query.flags.isDistinct) builder.appendKey(Keyword.DISTINCT)
                if (query.describe!!.projection != null) {
                    builder.append(' ').append(query.describe.projection.wrap())
                            .appendKey(Keyword.FROM)
                }
                builder.append(' ').append(printTarget(query.target))
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

        builder.append(' ').append(Keyword.WHERE.name).append(' ')

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

        val fieldPrinter = { field: String, aliasName: String? ->
            val internalBuilder = StringBuilder()
            internalBuilder.append(field.wrap())
            if (aliasName != null) {
                internalBuilder.append(" ").append(Keyword.AS.name).append(" ")
                internalBuilder.append(aliasName.wrap())
            }
            internalBuilder.toString()
        }

        when (selectProjection) {
            is SelectProjection.SingleField -> builder.append(fieldPrinter(selectProjection.field, selectProjection.newName))
            is SelectProjection.MultipleFields -> builder.append(selectProjection.fields.joinToString(", ", prefix = "(", postfix = ")", transform = { fieldPrinter(it.first, it.second) }))
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
            is Target.TargetQuery -> "(" + target.query.toString().trim() + ")"
        }
    }
}