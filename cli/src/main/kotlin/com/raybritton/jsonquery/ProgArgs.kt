package com.raybritton.jsonquery

data class ProgArgs(val input: String,
                    val output: String?,
                    val query: String,
                    val debug: Boolean,
                    val forApi: Boolean) {

    companion object {
        fun build(args: Array<String>): ProgArgs {
            var query: String? = null
            var input: String? = null
            var output: String? = null
            var debug = false
            var forApi = false

            var i = 0
            while (i < args.size) {
                when (args[i]) {
                    "-q" -> {
                        query = args[i + 1]
                        i++
                    }
                    "-i" -> {
                        input = args[i + 1]
                        i++
                    }
                    "-o" -> {
                        output = args[i + 1]
                        i++
                    }
                    "-d" -> {
                        debug = true
                    }
                    "-api" -> {
                        forApi = true
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

            return ProgArgs(input, output, query, debug, forApi)
        }
    }
}