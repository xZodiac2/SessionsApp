package com.ilya.sessions.screen.sessions

import com.ilya.data.retrofit.Session

sealed interface SessionScreenEvent {
    object Start : SessionScreenEvent
    object Retry : SessionScreenEvent
    data class Search(val query: String) : SessionScreenEvent
    data class AddFavourite(val session: Session) : SessionScreenEvent
}
