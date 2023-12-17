package com.ilya.sessions.screen

import com.ilya.data.retrofit.Session

sealed interface SessionsScreenEvent {
    object Start : SessionsScreenEvent
    object Retry : SessionsScreenEvent
    object Search : SessionsScreenEvent
    object Swipe : SessionsScreenEvent
    data class BackPress(val onConfirm: () -> Unit) : SessionsScreenEvent
    data class AddFavourite(val session: Session) : SessionsScreenEvent
    data class SearchInput(val value: String) : SessionsScreenEvent
}