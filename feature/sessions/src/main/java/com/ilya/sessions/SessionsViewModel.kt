package com.ilya.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.SessionsStateEvent
import com.ilya.core.TextReference
import com.ilya.data.SessionsRepository
import com.ilya.data.retrofit.Session
import com.ilya.sessions.models.group
import com.ilya.sessions.models.ungroup
import com.ilya.sessions.screen.SessionsScreenEvent
import com.ilya.sessions.screen.SessionsScreenState
import com.ilya.sessions.screen.alertDialog.AlertDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val repository: SessionsRepository,
) : ViewModel() {

    private val _screenStateFlow =
        MutableStateFlow<SessionsScreenState>(SessionsScreenState.Loading)
    val screenStateFlow = _screenStateFlow.asStateFlow()

    private val _snackbarEventStateFlow =
        MutableStateFlow<SessionsStateEvent>(SessionsStateEvent.Consumed)
    val snackbarEventStateFlow = _snackbarEventStateFlow.asStateFlow()

    private val _alertDialogStateFlow =
        MutableStateFlow<AlertDialogState>(AlertDialogState.Consumed)
    val alertDialogStateFlow = _alertDialogStateFlow.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _screenStateFlow.value = SessionsScreenState.Error(SessionsError.NoInternet)
    }

    private val sessionsList = mutableListOf<Session>()
    private var searchInputValue = ""

    fun getFavouritesList(): List<Session> {
        return sessionsList.filter { it.isFavourite }
    }

    fun handleEvent(event: SessionsScreenEvent) {
        when (event) {
            SessionsScreenEvent.Start -> onStart()
            SessionsScreenEvent.Retry -> onRetry()
            SessionsScreenEvent.Swipe -> onSwipe()
            is SessionsScreenEvent.AddFavourite -> onAddFavourite(event.session)
            is SessionsScreenEvent.Search -> onSearch(event.value)
            is SessionsScreenEvent.BackPress -> onBackPress(event.onConfirm)
        }
    }

    fun onSnackbarConsumed() {
        _snackbarEventStateFlow.value = SessionsStateEvent.Consumed
    }

    private fun onSwipe() {
        _isRefreshing.value = true

        when (_screenStateFlow.value) {
            is SessionsScreenState.ShowSearchedSessions -> onSearch(searchInputValue)
            else -> getAllSessions()
        }

        _isRefreshing.value = false
    }

    private fun onBackPress(onConfirm: () -> Unit) {
        _alertDialogStateFlow.value = AlertDialogState.SubmitExitRequest(
            onConfirm = {
                onConfirm()
                _alertDialogStateFlow.value = AlertDialogState.Consumed
            },
            onDismiss = {
                _alertDialogStateFlow.value = AlertDialogState.Consumed
            }
        )
    }

    private fun onStart() {
        if (_screenStateFlow.value == SessionsScreenState.Loading) {
            getAllSessions()
        }
    }

    private fun onRetry() {
        getAllSessions()
    }

    private fun onAddFavourite(session: Session) {
        if (isFavouritesFilled() && !session.isFavourite) {
            val text =
                TextReference.StringRef(R.string.unsuccessful_add, FAVOURITES_LIMIT.toString())
            _snackbarEventStateFlow.value = SessionsStateEvent.Triggered(text)
            return
        }

        when (val stateValue = _screenStateFlow.value) {
            is SessionsScreenState.ShowSessions -> {
                sessionsList.toggleFavourite(session)
                _screenStateFlow.value = SessionsScreenState.ShowSessions(sessionsList.group())
            }

            is SessionsScreenState.ShowSearchedSessions -> {
                sessionsList.toggleFavourite(session)
                val searchedSessions = stateValue.groupedSessions.ungroup().toMutableList()
                searchedSessions.toggleFavourite(session)
                _screenStateFlow.value =
                    SessionsScreenState.ShowSearchedSessions(searchedSessions.group())
            }

            else -> Unit
        }
    }

    private fun onSearch(searchBy: String) {
        _screenStateFlow.value = SessionsScreenState.Loading
        searchInputValue = searchBy

        if (searchBy.isBlank()) {
            _screenStateFlow.value = SessionsScreenState.ShowSessions(sessionsList.group())
            return
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val foundSessions = repository.searchSessions(searchBy.trim())
                .toMutableList()

            val favouriteIds = getFavouritesList().map { it.id }
            favouriteIds.forEach { favouriteId ->
                foundSessions.setIsFavourite(favouriteId, true)
            }

            _screenStateFlow.value = SessionsScreenState.ShowSearchedSessions(foundSessions.group())
        }
    }

    private fun getAllSessions() {
        _screenStateFlow.value = SessionsScreenState.Loading

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val sessions = repository.getAllSessions()
            val favouriteIds = getFavouritesList().map { it.id }
            sessionsList.clear()
            sessionsList.addAll(sessions)
            favouriteIds.forEach { sessionsList.setIsFavourite(it, true) }
            _screenStateFlow.value = SessionsScreenState.ShowSessions(sessionsList.group())
        }
    }


    private fun isFavouritesFilled(): Boolean {
        return getFavouritesList().size >= FAVOURITES_LIMIT
    }

    private fun MutableList<Session>.toggleFavourite(session: Session): MutableList<Session> {
        return setIsFavourite(session, !session.isFavourite)
    }

    private fun MutableList<Session>.setIsFavourite(
        session: Session,
        isFavourite: Boolean
    ): MutableList<Session> {
        return setIsFavourite(session.id, isFavourite)
    }

    private fun MutableList<Session>.setIsFavourite(
        sessionId: String,
        isFavourite: Boolean
    ): MutableList<Session> {
        forEachIndexed { sessionIndex, session ->
            if (session.id == sessionId) {
                this[sessionIndex] = session.copy(isFavourite = isFavourite)
                return this
            }
        }

        return this
    }

    companion object {
        private const val FAVOURITES_LIMIT = 3
    }

}