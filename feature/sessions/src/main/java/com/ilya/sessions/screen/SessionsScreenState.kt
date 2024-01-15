package com.ilya.sessions.screen

import com.ilya.sessions.SessionsError
import com.ilya.sessions.models.GroupedSessions

sealed interface SessionsScreenState {
    object Loading : SessionsScreenState
    data class Error(val error: SessionsError) : SessionsScreenState
    data class ShowSessions(val groupedSessions: List<GroupedSessions>) : SessionsScreenState
    data class ShowSearchedSessions(val groupedSessions: List<GroupedSessions>) : SessionsScreenState
}