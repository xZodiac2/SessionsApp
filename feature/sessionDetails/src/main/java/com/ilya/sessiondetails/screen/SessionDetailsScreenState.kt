package com.ilya.sessiondetails.screen

import com.ilya.data.retrofit.Session
import com.ilya.sessiondetails.SessionDetailsError

sealed interface SessionDetailsScreenState {
    object Loading : SessionDetailsScreenState
    data class ShowDetails(val session: Session) : SessionDetailsScreenState
    data class Error(val error: SessionDetailsError) : SessionDetailsScreenState
}
