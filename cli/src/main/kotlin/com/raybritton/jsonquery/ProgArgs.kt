package com.raybritton.jsonquery

data class ProgArgs(val input: String,
                    val output: String?,
                    val query: String,
                    val debug: Boolean) {

    companion object {
        fun build(args: Array<String>): ProgArgs {
            var query: String? = null
            var input: String? = null
            var output: String? = null
            var debug = false

            var i = 0
            while (i < args.size - 1) {
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
                }
                i++
            }

            if (query == null) {
                throw IllegalStateException("A query must be provided")
            }

            if (input == null) {
                throw IllegalStateException("A input file must be provided")
            }

            return ProgArgs(input, output, query, debug)
        }
    }
}