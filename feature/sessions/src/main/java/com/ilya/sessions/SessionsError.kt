package com.ilya.sessions

sealed interface SessionsError {
    object NoInternet : SessionsError
}
