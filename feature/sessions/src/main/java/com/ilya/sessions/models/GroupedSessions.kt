package com.ilya.sessions.models

import com.ilya.data.retrofit.Session


data class GroupedSessions(
    val date: String,
    val sessions: List<Session>,
)

fun List<Session>.group(): List<GroupedSessions> {
    val grouped = mutableMapOf<String, List<Session>>()

    this.forEach {
        val group = grouped[it.date]?.toMutableList() ?: mutableListOf()
        group += it
        grouped[it.date] = group
    }

    return grouped.map { (key, value) ->
        GroupedSessions(key, value)
    }
}

fun List<GroupedSessions>.ungroup(): List<Session> = this.map { it.sessions }.flatten()