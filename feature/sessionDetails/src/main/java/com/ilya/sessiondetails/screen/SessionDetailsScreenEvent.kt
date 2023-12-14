package com.ilya.sessiondetails.screen

interface SessionDetailsScreenEvent {
    data class Start(val sessionId: String) : SessionDetailsScreenEvent
    object Retry : SessionDetailsScreenEvent
}