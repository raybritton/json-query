package com.raybritton.jsonquery

import java.io.File

internal class JsonLoader() {
    fun load(input: String): String {
        val input = input.trim()
        when {
            input.startsWith("{") -> { return input }
            input.startsWith("file") -> { return loadFromFile(input) }
            else -> throw IllegalStateException("$input is not supported")
        }
    }

    private fun loadFromFile(path: String): String {
        val file = File(path)
        return file.readLines().joinToString("")
    }
}