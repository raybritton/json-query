package com.raybritton.jsonquery.models

data class ProgArgs(val input: String,
            val output: String?,
            val query: String) {

    companion object {
        fun build(args: Array<String>): ProgArgs {
            var query: String? = null
            var input: String? = null

            var i = 0
            while (i < args.size - 1) {
                when {
                    args[i] == "-q" -> {
                        query = args[i + 1]
                        i++
                    }
                    args[i] == "-i" -> {
                        input = args[i + 1]
                        i++
                    }
                }
                i++
            }

            if (query == null) {
                throw IllegalStateException("A query must be provided")
            }

            if (input == null) {
                throw IllegalStateException("A input file must be provided")
            }

            return ProgArgs(input, null, query)
        }
    }
}