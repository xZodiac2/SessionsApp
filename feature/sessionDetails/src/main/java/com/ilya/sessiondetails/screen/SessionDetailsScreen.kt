package com.ilya.sessiondetails.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ilya.data.retrofit.Session
import com.ilya.sessiondetails.R
import com.ilya.sessiondetails.SessionDetailsError
import com.ilya.sessiondetails.SessionDetailsViewModel
import com.ilya.theme.LocalColorScheme
import com.ilya.theme.LocalTypography

@Composable
fun SessionDetailsScreen(
    sessionId: String,
    onBackClick: () -> Unit,
    detailsViewModel: SessionDetailsViewModel = hiltViewModel(),
) {
    val state = detailsViewModel.screenStateFlow.collectAsState()
    
    when (val stateValue = state.value) {
        SessionDetailsScreenState.Loading -> LoadingState()
        is SessionDetailsScreenState.Error -> ErrorState(
            error = stateValue.error,
            onTryAgainClick = {
                detailsViewModel.handleEvent(SessionDetailsScreenEvent.Retry)
            },
            onBackClick = onBackClick
        )
        
        is SessionDetailsScreenState.ShowDetails -> ShowDetailsState(stateValue.session)
    }
    
    BackHandler(onBack = onBackClick)
    
    LaunchedEffect(key1 = Unit, block = {
        detailsViewModel.handleEvent(SessionDetailsScreenEvent.Start(sessionId))
    })
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(error: SessionDetailsError, onTryAgainClick: () -> Unit, onBackClick: () -> Unit) {
    when (error) {
        is SessionDetailsError.NoInternet -> NoInternetError(onTryAgainClick, onBackClick)
        is SessionDetailsError.NoId -> NoIdError(onTryAgainClick, onBackClick)
    }
}

@Composable
private fun NoInternetError(onTryAgainClick: () -> Unit, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_no_internet),
            color = LocalColorScheme.current.primaryTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        Button(onClick = onTryAgainClick) {
            Text(text = stringResource(id = R.string.try_again))
        }
        Button(
            modifier = Modifier.padding(top = 20.dp),
            onClick = onBackClick
        ) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}

@Composable
private fun NoIdError(onTryAgainClick: () -> Unit, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_no_id),
            color = LocalColorScheme.current.primaryTextColor,
            textAlign = TextAlign.Center
        )
        Button(onClick = onTryAgainClick) {
            Text(text = stringResource(id = R.string.try_again))
        }
        Button(
            modifier = Modifier.padding(top = 20.dp),
            onClick = onBackClick
        ) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}

@Composable
private fun ShowDetailsState(session: Session) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(300.dp),
                model = session.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = session.speaker,
                fontSize = LocalTypography.current.largeFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = LocalColorScheme.current.primaryTextColor
            )
        }
        Column(
            modifier = Modifier.padding(start = 32.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_24),
                    contentDescription = null,
                    tint = LocalColorScheme.current.primaryTextColor
                )
                Text(
                    text = "${session.date}, ${session.timeInterval}",
                    fontSize = LocalTypography.current.lowFontSize,
                    color = LocalColorScheme.current.secondaryTextColor
                )
            }
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = session.description,
                fontSize = LocalTypography.current.defaultFontSize,
                color = LocalColorScheme.current.secondaryTextColor
            )
        }
    }
}