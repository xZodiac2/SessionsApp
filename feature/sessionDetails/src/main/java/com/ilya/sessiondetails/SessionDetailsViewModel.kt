package com.ilya.sessiondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.data.SessionsRepository
import com.ilya.sessiondetails.screen.SessionDetailsScreenEvent
import com.ilya.sessiondetails.screen.SessionDetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionDetailsViewModel @Inject constructor(
    private val repository: SessionsRepository,
) : ViewModel() {
    
    private val _screenStateFlow = MutableStateFlow<SessionDetailsScreenState>(SessionDetailsScreenState.Loading)
    val screenStateFlow = _screenStateFlow.asStateFlow()
    
    private var sessionId: Int = -1
    
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        if (exception is NoSuchElementException) {
            _screenStateFlow.value = SessionDetailsScreenState.Error(SessionDetailsError.NoId)
        } else {
            _screenStateFlow.value = SessionDetailsScreenState.Error(SessionDetailsError.NoInternet)
        }
    }
    
    fun handleEvent(event: SessionDetailsScreenEvent) {
        when (event) {
            is SessionDetailsScreenEvent.Start -> onStart(event.sessionId)
            is SessionDetailsScreenEvent.Retry -> onRetry()
        }
    }
    
    private fun onRetry() {
        getSessionById(sessionId)
    }
    
    private fun onStart(id: Int) {
        if (_screenStateFlow.value is SessionDetailsScreenState.Loading) {
            getSessionById(id)
        }
    }
    
    private fun getSessionById(id: Int) {
        _screenStateFlow.value = SessionDetailsScreenState.Loading
        sessionId = id
        
        viewModelScope.launch(exceptionHandler) {
            val session = repository.getSessionById(sessionId)
            _screenStateFlow.value = SessionDetailsScreenState.ShowDetails(session)
        }
    }
    
}