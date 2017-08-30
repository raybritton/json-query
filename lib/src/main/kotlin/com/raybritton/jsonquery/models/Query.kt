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
                          val pretty: Boolean = false,
                          val withKeys: Boolean = false,
                          val order: String? = null) {
    enum class Method(val keyword: String) {
        DESCRIBE("DESCRIBE"), SELECT("SELECT"), DISTINCT("SELECT DISTINCT");

        companion object {
            fun getMethodByKeyword(keyword: String): Method {
                for (method in Method.values()) {
                    if (method.keyword == keyword) {
                        return method
                    }
                }
                throw IllegalArgumentException("Method $keyword not supported")
            }
        }
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