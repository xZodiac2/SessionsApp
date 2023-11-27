package com.ilya.domain.sessions

import com.ilya.core.UseCase
import com.ilya.data.SessionsRepository
import com.ilya.domain.sessions.models.ReceivedSession
import com.ilya.domain.sessions.models.Sessions
import javax.inject.Inject

class GetSessionsUseCase @Inject constructor(
    private val repository: SessionsRepository,
) : UseCase<String?, Sessions> {
    
    override suspend operator fun invoke(data: String?): Result<Sessions> {
        val sessions = (data?.let { repository.searchSessions(it) } ?: repository.getAllSessions())
            .map { ReceivedSession.createFrom(it) }
        
        val favourites = sessions.filter { it.isFavourite }
        
        return Result.success(Sessions(sessions.toMutableList(), favourites.toMutableList()))
    }
    
}