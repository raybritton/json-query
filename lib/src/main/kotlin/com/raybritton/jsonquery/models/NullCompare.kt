package com.raybritton.jsonquery.models

class NullCompare {
    override fun equals(other: Any?): Boolean {
        return (other is NullCompare)
    }

    override fun hashCode(): Int {
        return 1
    }
}