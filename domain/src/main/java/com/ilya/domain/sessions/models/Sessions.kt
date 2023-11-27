package com.ilya.domain.sessions.models

import com.ilya.data.retrofit.Session

data class Sessions(
    val allSessions: MutableList<ReceivedSession>,
    val favourites: MutableList<ReceivedSession>,
)

data class ReceivedSession(
    val date: String,
    val imageUrl: String,
    val speaker: String,
    val description: String,
    val timeInterval: String,
    val id: String,
    val isFavourite: Boolean,
) {
    internal companion object {
        fun createFrom(session: Session): ReceivedSession {
            return ReceivedSession(
                session.date,
                session.imageUrl,
                session.speaker,
                session.description,
                session.timeInterval,
                session.id,
                session.isFavourite
            )
        }
    }
}