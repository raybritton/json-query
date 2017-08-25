package com.raybritton.jsonquery

import java.io.File

class JsonLoader() {
    fun load(input: String): String {
        when {
            input.startsWith("{") -> { return input }
            input.startsWith("file") -> { return loadFromFile(input) }
            else -> error("$input is not supported")
        }
    }

    fun loadFromFile(path: String): String {
        val file = File(path)
        return file.readLines().joinToString("")
    }
}