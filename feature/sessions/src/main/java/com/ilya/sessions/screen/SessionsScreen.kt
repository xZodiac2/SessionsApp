package com.ilya.sessions.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilya.data.retrofit.Session
import com.ilya.sessions.R
import com.ilya.sessions.SessionsViewModel
import com.ilya.sessions.screen.favourite.favourites
import com.ilya.sessions.screen.sessions.SessionScreenEvent
import com.ilya.sessions.screen.sessions.sessions
import com.ilya.theme.LocalColorScheme

@Composable
fun SessionsScreen(
    modifier: Modifier = Modifier,
    onSessionClick: (Session) -> Unit,
    onUnsuccessfulAdd: () -> Unit,
    viewModel: SessionsViewModel = hiltViewModel(),
) {
    val sessionsStateFlow by viewModel.sessionsStateFlow.collectAsState()
    val favouriteStateFlow by viewModel.favouritesStateFlow.collectAsState()
    
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        favourites(favouriteStateFlow)
        item {
            Text(
                text = stringResource(id = R.string.sessions_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColorScheme.current.primaryTextColor
            )
        }
        sessions(
            sessionsState = sessionsStateFlow,
            onUnsuccessfulAdd = onUnsuccessfulAdd,
            onSessionClick = onSessionClick,
            onTryAgainClick = { viewModel.handleEvent(SessionScreenEvent.Retry) },
            onFavouriteClick = { session ->
                viewModel.handleEvent(SessionScreenEvent.AddFavourite(session))
                viewModel.favouritesAddStatus
            }
        )
    }
    
    LaunchedEffect(key1 = Unit, block = {
        viewModel.handleEvent(SessionScreenEvent.Start)
    })
}
