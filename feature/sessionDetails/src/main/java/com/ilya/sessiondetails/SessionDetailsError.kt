package com.ilya.sessiondetails

sealed interface SessionDetailsError {
    object NoId : SessionDetailsError
    object NoInternet : SessionDetailsError
}
