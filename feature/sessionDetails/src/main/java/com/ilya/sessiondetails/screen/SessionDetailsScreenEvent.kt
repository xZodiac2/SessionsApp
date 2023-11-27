package com.ilya.sessiondetails.screen

interface SessionDetailsScreenEvent {
    data class Start(val sessionId: Int) : SessionDetailsScreenEvent
    object Retry : SessionDetailsScreenEvent
}