package com.ilya.sessions.screen

import com.ilya.data.retrofit.Session

sealed interface SessionsScreenEvent {
    object Start : SessionsScreenEvent
    object Retry : SessionsScreenEvent
    object Swipe : SessionsScreenEvent
    data class Search(val value: String) : SessionsScreenEvent
    data class BackPress(val onConfirm: () -> Unit) : SessionsScreenEvent
    data class AddFavourite(val session: Session) : SessionsScreenEvent
}