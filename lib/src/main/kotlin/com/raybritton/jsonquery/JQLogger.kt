package com.raybritton.jsonquery

object JQLogger {
    enum class Level {
        NONE, DEBUG, INFO
    }

    var level = Level.NONE

    internal fun debug(message: String) {
        if (level >= Level.DEBUG) println(message)
    }

    internal fun info(message: String) {
        if (level >= Level.INFO) println(message)
    }
}