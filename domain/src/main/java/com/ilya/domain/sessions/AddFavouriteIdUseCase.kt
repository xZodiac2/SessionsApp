package com.ilya.domain.sessions

import com.ilya.core.UseCase
import com.ilya.data.SessionsRepository
import javax.inject.Inject

class AddFavouriteIdUseCase @Inject constructor(
    private val repository: SessionsRepository,
) : UseCase<Int, Unit> {
    
    override suspend operator fun invoke(data: Int): Result<Unit> {
        repository.addFavouriteId(data)
        return Result.success(Unit)
    }
    
}