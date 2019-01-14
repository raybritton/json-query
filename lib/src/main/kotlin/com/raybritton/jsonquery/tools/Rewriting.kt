package com.raybritton.jsonquery.tools

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.ext.copyWithoutFirst
import com.raybritton.jsonquery.ext.copyWithoutLast
import com.raybritton.jsonquery.ext.toPath
import com.raybritton.jsonquery.ext.toSegments
import com.raybritton.jsonquery.models.JsonArray
import com.raybritton.jsonquery.models.JsonObject
import com.raybritton.jsonquery.models.Query
import com.raybritton.jsonquery.models.SelectProjection
import java.util.*

/**
 * Methods in this file update the object they are called instead of returning a new version
 */
internal fun Any?.rewrite(query: Query) {
    if (this == null) return
    if (query.select?.projection == null) return

    val projection = query.select.projection
    when (projection) {
        is SelectProjection.SingleField -> {
            if (projection.newName != null) {
                update(projection.field, projection.newName)
            }
        }
        is SelectProjection.MultipleFields -> {
            projection.fields.forEach {
                if (it.second != null) {
                    update(it.first, it.second!!)
                }
            }
        }
    }
}

private fun Any.rename(segments: List<String>, newName: String) {
    val key = segments.last()
    val path = segments.copyWithoutLast()
    val container = this.navigateToTarget(path.joinToString("."))
    when (container) {
        is JsonObject -> {
            container[newName] = container[key]
            container.remove(key)
        }
        is JsonArray -> {
            container.filter { it is JsonObject }
                    .forEach {
                        val element = it as JsonObject
                        element[newName] = element[key]
                        element.remove(key)
                    }
        }
        else -> throw RuntimeException("Aliasing $key to $newName is not possible as ${container.javaClass} is not in an object")
    }
}

private fun Any.createPath(new: List<String>) {
    if (new.isNotEmpty()) {
        val segment = new[0]
        when {
            this is JsonObject -> {
                if (this[segment] !is JsonObject) {
                    this[segment] = JsonObject()
                }
                this[segment]!!.createPath(new.copyWithoutFirst())
            }
            this is JsonArray -> {
                forEach {
                    if (it is JsonObject) {
                        it.createPath(new.copyWithoutFirst())
                    }
                }
            }
            else -> throw RuntimeException("Unable to rename to ${new.joinToString(".")}, encountered: ${this.javaClass}")
        }
    }
}

private fun Any.move(old: List<String>, new: List<String>) {
    val key = old.last()
    val oldSegments = old.copyWithoutLast()
    val oldContainer = this.navigateToTarget(oldSegments.toPath())
    when (oldContainer) {
        is JsonObject -> {
            val value = oldContainer.remove(key)
            if (oldContainer.isEmpty()) {
                if (oldSegments.isNotEmpty()) {
                    val parent = this.navigateToTarget(oldSegments.copyWithoutLast().toPath())
                    when (parent) {
                        is JsonObject -> parent.remove(oldSegments.last())
                        is JsonArray -> {
                        }
                        else -> throw IllegalStateException("Parent was a value: $parent (${parent.javaClass})")
                    }
                }
            }
            this.createPath(new)
            val newContainer = this.navigateToTarget(new.copyWithoutLast().toPath())
            if (newContainer is JsonObject) {
                newContainer[new.last()] = value
            } else {
                throw IllegalStateException("createPath() didn't force target to JsonObject")
            }
            this.removeEmpties()
        }
        is JsonArray -> {
            val values = ArrayDeque(oldContainer.filter { it is JsonObject }.map { (it as JsonObject).remove(key) })
            if (this is JsonArray) {
                (0 until values.size).forEach {
                    when {
                        this.size < it -> this.add(JsonObject())
                        this[it] !is JsonObject -> this[it] = JsonObject()
                    }
                }
                this.filter { it is JsonObject }.forEach {
                    it!!.createPath(new)
                    val newContainer = it.navigateToTarget(new.copyWithoutLast().toPath())
                    if (newContainer is JsonObject) {
                        newContainer[new.last()] = values.poll()
                    } else {
                        throw IllegalStateException("createPath() didn't force target to JsonObject: ${newContainer.javaClass}")
                    }
                }
            } else if (this is JsonObject) {
                this[new.last()] = JsonArray(values)
//                this.createPath(new)
//                (this.navigateToTarget(new.copyWithoutLast().toPath()) as JsonObject)[new.last()] = JsonArray(values)
            }

            this.removeEmpties()
        }
        else -> throw RuntimeException("Aliasing $key to ${new.last()} is not possible as ${oldContainer.javaClass} is not in an object")
    }
}

private fun Any?.removeEmpties() {
    val isEmpty: (Any?) -> Boolean = {
        ((it as? JsonArray)?.isEmpty() == true) || ((it as? JsonObject)?.isEmpty() == true)
    }

    when (this) {
        is JsonArray -> {
            val iterator = this.listIterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (isEmpty(element)) {
                    iterator.remove()
                } else {
                    element.removeEmpties()
                    if (isEmpty(element)) {
                        iterator.remove()
                    }
                }
            }
        }
        is JsonObject -> {
            val iterator = this.keys.iterator()
            while (iterator.hasNext()) {
                val key = iterator.next()
                if (isEmpty(this[key])) {
                    iterator.remove() //call iterator.remove() before to stop ConcurrentModificationException
                    this.remove(key)
                } else {
                    this[key].removeEmpties()
                    if (isEmpty(this[key])) {
                        iterator.remove()
                        this.remove(key)
                    }
                }
            }
        }
    }
}


private fun Any.update(field: String, newName: String) {
    val currentPath = field.toSegments()
    val newPath = newName.toSegments()

    if (currentPath.copyWithoutLast() == newPath.copyWithoutLast()) {
        rename(currentPath, newPath.last())
    } else {
        move(currentPath, newPath)
    }
}