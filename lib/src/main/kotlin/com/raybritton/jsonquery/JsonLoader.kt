package com.raybritton.jsonquery

import java.io.File
import java.net.URL
import kotlin.streams.toList

internal class JsonLoader() {
    fun load(input: String): String {
        val input = input.trim()
        when {
            input.startsWith("{") -> { return input }
            input.startsWith("http") -> { return loadFromUrl(input) }
            else -> { return loadFromFile(input) }
        }
    }

    private fun loadFromFile(path: String): String {
        val file = File(path)
        return file.readLines().joinToString("")
    }

    private fun loadFromUrl(url: String): String {
        return URL(url).openStream()
                .bufferedReader()
                .lines()
                .toList()
                .joinToString("")
    }
}