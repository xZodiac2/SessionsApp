package com.ilya.sessions.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ilya.EventEffect
import com.ilya.data.retrofit.Session
import com.ilya.sessions.R
import com.ilya.sessions.SessionsViewModel
import com.ilya.sessions.screen.alertDialog.AlertDialogStateHandler
import com.ilya.sessions.screen.favourites.Favourites
import com.ilya.sessions.screen.search.SearchContent
import com.ilya.sessions.screen.sessions.Sessions
import com.ilya.theme.LocalColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    onSessionClick: (String) -> Unit,
    quit: () -> Unit,
    sessionsViewModel: SessionsViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarEvent by sessionsViewModel.snackbarEventStateFlow.collectAsState()
    val alertDialogState by sessionsViewModel.alertDialogStateFlow.collectAsState()
    val isRefreshing by sessionsViewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    
    BackHandler {
        sessionsViewModel.handleEvent(SessionsScreenEvent.BackPress(quit))
    }
    AlertDialogStateHandler(alertDialogState)
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalColorScheme.current.primary
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { sessionsViewModel.handleEvent(SessionsScreenEvent.Swipe) }
        ) {
            Content(
                sessionsViewModel = sessionsViewModel,
                onSessionClick = onSessionClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
    
    EventEffect(
        event = snackbarEvent,
        onConsumed = sessionsViewModel::onSnackbarConsumed,
        action = { snackbarHostState.showSnackbar(it) }
    )
    
    LaunchedEffect(key1 = Unit) {
        sessionsViewModel.handleEvent(SessionsScreenEvent.Start)
    }
}

@Composable
private fun Content(
    sessionsViewModel: SessionsViewModel,
    onSessionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SearchContent(
            onSearch = { sessionsViewModel.handleEvent(SessionsScreenEvent.Search(it)) }
        )
        
        FavouritesContent(
            favouritesList = sessionsViewModel.getFavouritesList(),
            onSessionClick = onSessionClick
        )
        
        val sessionsState by sessionsViewModel.screenStateFlow.collectAsState()
        SessionsContent(
            sessionsState = sessionsState,
            onSessionClick = onSessionClick,
            onFavouriteClick = {
                sessionsViewModel.handleEvent(SessionsScreenEvent.AddFavourite(it))
            },
            onTryAgainClick = {
                sessionsViewModel.handleEvent(SessionsScreenEvent.Retry)
            }
        )
    }
}

@Composable
private fun FavouritesContent(
    favouritesList: List<Session>,
    onSessionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (favouritesList.isNotEmpty()) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = stringResource(id = R.string.favorites_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColorScheme.current.primaryTextColor
        )
        Favourites(
            favouriteList = favouritesList,
            onSessionClick = onSessionClick,
            modifier = modifier
        )
    }
}

@Composable
private fun SessionsContent(
    sessionsState: SessionsScreenState,
    onSessionClick: (String) -> Unit,
    onFavouriteClick: (Session) -> Unit,
    onTryAgainClick: () -> Unit,
) {
    Text(
        modifier = Modifier.padding(start = 20.dp),
        text = stringResource(id = R.string.sessions_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = LocalColorScheme.current.primaryTextColor
    )
    Sessions(sessionsState, onFavouriteClick, onSessionClick, onTryAgainClick)
}