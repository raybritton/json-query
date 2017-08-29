package com.raybritton.jsonquery.models

internal data class Query(val method: Method,
                          val target: String,
                          val targetExtra: TargetExtra? = null,
                          val targetKeys: List<String> = listOf(),
                          val skip: Int? = null,
                          val limit: Int? = null,
                          val where: Where? = null,
                          val asJson: Boolean = false,
                          val desc: Boolean = false,
                          val pretty: Boolean = false,
                          val withKeys: Boolean = false,
                          val order: String? = null) {
    enum class Method {
        DESCRIBE, SELECT
    }

    enum class TargetExtra {
        KEY, VALUES, SPECIFIC
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
}