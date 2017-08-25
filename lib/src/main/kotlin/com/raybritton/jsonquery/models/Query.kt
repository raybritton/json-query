package com.raybritton.jsonquery.models

data class Query(val method: Method,
                 val target: String,
                 val skip: Int = 0,
                 val limit: Int = 0,
                 val where: Where? = null,
                 val asJson: Boolean = false,
                 val withKeys: Boolean = false) {
    enum class Method {
        DESCRIBE, LIST, GET
    }

    data class Where(val target: String,
                     val operator: Operator,
                     val compare: String) {
        enum class Operator(val symbol: String) {
            EQUAL("=="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">")
        }

        companion object {
            fun getOperatorBySymbol(symbol: String): Where.Operator {
                for (op in Where.Operator.values()) {
                    if (op.symbol == symbol) {
                        return op
                    }
                }
                error("Operator $symbol not supported")
            }
        }
    }
}