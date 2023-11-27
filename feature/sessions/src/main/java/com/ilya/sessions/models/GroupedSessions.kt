package com.ilya.sessions.models

import com.ilya.data.retrofit.Session


data class GroupedSessions(
    val date: String,
    val sessions: List<Session>,
)