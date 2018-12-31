package com.raybritton.jsonquery.printers

import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.Query.Method.*

internal interface Printer {
    fun print(json: Any, query: Query): String

    companion object {
        fun createPrinter(query: Query): Printer {
            return when (query.method) {
                SELECT -> SelectPrinter
                DESCRIBE -> DescribePrinter
                SEARCH -> SearchPrinter
            }
        }
    }
}