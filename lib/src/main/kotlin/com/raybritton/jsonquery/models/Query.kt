package com.raybritton.jsonquery.models

data class Query(val method: Method,
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
        KEYS, VALUES, SPECIFIC, MIN, MAX, COUNT, SUM, VALUE, KEY
    }

    data class Where(val field: String,
                     val operator: Operator,
                     val compare: Any?) {
        enum class Operator(val symbol: String) {
            EQUAL("=="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), CONTAINS("#"), NOT_CONTAINS("!#")
        }

        companion object {
            fun getOperatorBySymbol(symbol: String): Where.Operator {
                for (op in Where.Operator.values()) {
                    if (op.symbol == symbol) {
                        return op
                    }
                }
                throw IllegalArgumentException("Operator $symbol not supported")
            }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        if (method == Method.SEARCH) {
            builder.append("SEARCH ")
            builder.appendWrapped(target)
            builder.append(" FOR ")
            builder.append(targetExtra)
            builder.append(" ")
            if (targetKeys[0][0].isLetter()) {
                builder.appendWrapped(targetKeys[0])
            } else {
                builder.append(targetKeys[0])
            }
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
                        if (targetKeys.size == 1) {
                            builder.appendWrapped(targetKeys[0])
                        } else {
                            targetKeys.joinTo(builder, ", ", "(", ")", transform = { '"' + it + '"' })
                        }
                    }
                }
                builder.append(" FROM ")
            }
            builder.appendWrapped(target)
            if (where != null) {
                builder.append(" WHERE ")
                builder.appendWrapped(where.field)
                builder.append(" ")
                builder.append(where.operator.symbol)
                builder.append(" ")
                if (where.compare is String) {
                    builder.appendWrapped(where.compare)
                } else {
                    builder.append(where.compare)
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

    class Builder {
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