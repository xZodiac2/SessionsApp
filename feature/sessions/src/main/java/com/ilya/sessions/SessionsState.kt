package com.ilya.sessions

import com.ilya.data.retrofit.Session


sealed interface SessionsState {
    object Loading : SessionsState
    data class Error(val error: SessionsError) : SessionsState
    data class ShowSessions(val sessions: MutableList<Session>) : SessionsState
}
