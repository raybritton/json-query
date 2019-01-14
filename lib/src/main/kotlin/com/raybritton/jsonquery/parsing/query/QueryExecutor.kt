package com.raybritton.jsonquery.parsing.query

import com.raybritton.jsonquery.RuntimeException
import com.raybritton.jsonquery.ext.sort
import com.raybritton.jsonquery.models.*
import com.raybritton.jsonquery.models.Target
import com.raybritton.jsonquery.tools.*

/**
 * This class runs all the queries in order (deepest first) so the parent queries has the values needed
 */

internal class QueryExecutor {
    fun execute(json: Any, query: Query): Any {
        var targetJson = json
        var updatedQuery = query
        if (query.target is Target.TargetQuery) {
            targetJson = execute(json, query.target.query)
            updatedQuery = query.copy(target = Target.TargetField("."))
        }

        return process(targetJson, updatedQuery)
    }

    private fun process(json: Any, query: Query): Any {
        val target = (query.target as Target.TargetField).value
        var updatedQuery = query

        //NAVIGATE
        var targetJson = json.navigateToTarget(target)

        targetJson = checkOffset(targetJson, query)

        if (updatedQuery.where != null) {
            (updatedQuery.where?.value as? Value.ValueQuery)?.let {
                val value = execute(targetJson, it.value)
                updatedQuery = updatedQuery.copy(
                        where = updatedQuery.where!!.copy(
                                value = Value.ValueString(value.toString())
                        ))
            }
        }

        //FILTER
        var workingJson: Any? = if (updatedQuery.where != null) targetJson.where(updatedQuery.where!!, updatedQuery.flags.isCaseSensitive, query.select?.offset) else targetJson

        if (workingJson == null) throw RuntimeException("Where returned null")

        workingJson = checkLimit(workingJson, query)

        //MATH
        (updatedQuery.select?.projection as? SelectProjection.Math)?.let {
            return workingJson!!.math(it.expr, it.field, updatedQuery.flags.isByElement)
        }

        //OUTPUT
        if (updatedQuery.flags.isDistinct) {
            if (workingJson is JsonArray) {
                workingJson = workingJson.distinct()
            }
        }

        if (updatedQuery.select?.orderBy != null) {
            workingJson = workingJson.sort(updatedQuery)
        }

        workingJson = workingJson?.filterToProjection(updatedQuery)

        workingJson.rewrite(updatedQuery)

        return workingJson!!
    }

    private fun checkLimit(json: Any, query: Query): Any {
        val limit = when (query.method) {
            Query.Method.SELECT -> query.select!!.limit
            Query.Method.DESCRIBE -> query.describe!!.limit
            else -> null
        }

        if (limit != null) {
            if (json is JsonArray) {
                return json.subList(0, Math.min(limit, json.size))
            } else {
                throw RuntimeException("Tried to LIMIT on ${json::class.java}", RuntimeException.ExtraInfo.LIMIT_OBJECT)
            }
        }

        return json
    }

    private fun checkOffset(json: Any, query: Query): Any {
        val offset = when (query.method) {
            Query.Method.SELECT -> query.select!!.offset
            Query.Method.DESCRIBE -> query.describe!!.offset
            else -> null
        }

        if (offset != null) {
            if (json is JsonArray) {
                return json.subList(0, Math.min(offset, json.size - offset))
            } else {
                throw RuntimeException("Tried to OFFSET on ${json::class.java}", RuntimeException.ExtraInfo.OFFSET_OBJECT)
            }
        }

        return json
    }
}