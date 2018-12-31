package com.raybritton.jsonquery.printers

import com.raybritton.jsonquery.models.Query

internal object SearchPrinter : Printer {
    override fun print(json: Any, query: Query): String {
        return ""
    }
}