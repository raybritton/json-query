package com.raybritton.jsonquery.models

internal data class Query(val method: Method,
                 val target: String,
                 val targetExtra: TargetExtra? = null,
                 val targetKeys: List<String> = listOf(),
                 val offset: Int? = null,
                 val limit: Int? = null,
                 val where: Where? = null,
                 val asJson: Boolean = false,
                 val desc: Boolean = false,
                 val distinct: Boolean = false,
                 val pretty: Boolean = false,
                 val withKeys: Boolean = false,
                 val order: String? = null) {
    enum class Method {
        DESCRIBE, SELECT, SEARCH
    }

    enum class TargetExtra {
        /**
         * Only return keys for this query
         */
        KEYS,
        /**
         * Only return values for this query
         */
        VALUES,
        /**
         * INTERNAL
         * Query specifies columns to return
         */
        SPECIFIC,
        /**
         * Return lowest numeric value from queried fields
         */
        MIN,
        /**
         * Return highed numeric value from queried fields
         */
        MAX,
        /**
         * Return number of fields matching query
         */
        COUNT,
        /**
         * Return sum of all numeric values from queried fields
         */
        SUM,
        /**
         * For search
         * Search values only for specified string
         */
        VALUE,
        /**
         * For search
         * Search keys only for specified string
         */
        KEY,
        /**
         * For search
         * Search keys and values for specified string
         */
        BOTH
    }

    data class Where(val field: String,
                     val operator: Operator,
                     val compare: Any?) {
        enum class Operator(vararg val symbol: String) {
            EQUAL("==", "="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), CONTAINS("#"), NOT_CONTAINS("!#")
        }

        companion object {
            @Throws(IllegalArgumentException::class)
            fun getOperatorBySymbol(symbol: String): Where.Operator {
                for (op in Where.Operator.values()) {
                    if (op.symbol.contains(symbol)) {
                        return op
                    }
                }
                throw IllegalArgumentException("Operator $symbol not supported")
            }
        }

        class Builder {
            var field: String? = null
            var operator: Operator? = null
            var compare: Any? = null

            fun build(): Where {
                return Where(field!!, operator!!, compare)
            }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        if (method == Method.SEARCH) {
            builder.append("SEARCH ")
            builder.appendWrapped(target)
            builder.append(" FOR ")
            if (targetExtra != TargetExtra.BOTH) {
                builder.append(targetExtra)
                builder.append(" ")
            }
            builder.appendWrapped(targetKeys[0])
        } else {
            builder.append(method)
            builder.append(" ")
            if (distinct) {
                builder.append("DISTINCT ")
            }
            if (targetExtra != null) {
                when (targetExtra) {
                    TargetExtra.VALUES, TargetExtra.KEYS -> {
                        builder.append(targetExtra)
                    }
                    TargetExtra.MIN, TargetExtra.MAX, TargetExtra.COUNT, TargetExtra.SUM -> {
                        builder.append(targetExtra)
                        builder.appendWrapped(targetKeys[0], true)
                    }
                    TargetExtra.SPECIFIC -> {
                        when (targetKeys.size) {
                            0 -> {}
                            1 -> builder.appendWrapped(targetKeys[0])
                            else -> targetKeys.joinTo(builder, ", ", "(", ")", transform = { '"' + it + '"' })
                        }
                    }
                }
                if (targetExtra != TargetExtra.SPECIFIC || (targetExtra == TargetExtra.SPECIFIC && targetKeys.isNotEmpty())) {
                    builder.append(" FROM ")
                }
            }
            builder.appendWrapped(target)
            if (where != null) {
                builder.append(" WHERE ")
                builder.appendWrapped(where.field)
                builder.append(" ")
                builder.append(where.operator.symbol[0])
                builder.append(" ")
                if (where.compare.toString()[0].isDigit()) {
                    builder.append(where.compare)
                } else {
                    builder.appendWrapped(where.compare.toString())
                }
            }
            if (order != null) {
                builder.append(" ORDER BY ")
                builder.appendWrapped(order)
                if (desc) {
                    builder.append(" DESC")
                }
            }
            if (limit != null) {
                builder.append(" LIMIT ")
                builder.append(limit)
            }
            if (offset != null) {
                builder.append(" OFFSET ")
                builder.append(offset)
            }
            if (withKeys) {
                builder.append(" WITH KEYS")
            }
            if (asJson) {
                builder.append(" AS JSON")
            }
            if (pretty) {
                builder.append(" PRETTY")
            }
        }
        return builder.toString()
    }

    private fun StringBuilder.appendWrapped(msg: String, brackets: Boolean = false) {
        if (brackets) append("(")
        append('"')
        append(msg)
        append('"')
        if (brackets) append(")")
    }

    internal class Builder {
        var method: Method? = null
        var target: String? = null
        var targetExtra: TargetExtra? = null
        var targetKeys: List<String> = listOf()
        var offset: Int? = null
        var limit: Int? = null
        var where: Where? = null
        var asJson: Boolean = false
        var desc: Boolean = false
        var distinct: Boolean = false
        var pretty: Boolean = false
        var withKeys: Boolean = false
        var order: String? = null

        fun build() = Query(method!!, target!!, targetExtra, targetKeys, offset, limit, where, asJson, desc, distinct, pretty, withKeys, order)
    }
}