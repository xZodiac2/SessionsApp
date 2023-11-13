package com.ilya.sessions.screen.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ilya.data.retrofit.Session
import com.ilya.sessions.R
import com.ilya.sessions.SessionsError
import com.ilya.sessions.SessionsState
import com.ilya.theme.LocalColorScheme


fun LazyListScope.sessions(
    sessionsState: SessionsState,
    onUnsuccessfulAdd: () -> Unit,
    onTryAgainClick: () -> Unit,
    onSessionClick: (Session) -> Unit,
    onFavouriteClick: (Session) -> Boolean,
) {
    when (sessionsState) {
        is SessionsState.Loading -> loadingState()
        is SessionsState.Error -> errorState(sessionsState.error, onTryAgainClick)
        is SessionsState.ShowSessions -> showSessionsState(
            sessionsState.sessions, onUnsuccessfulAdd, onSessionClick, onFavouriteClick
        )
    }
}

private fun LazyListScope.showSessionsState(
    sessions: List<Session>,
    onUnsuccessfulAdd: () -> Unit,
    onSessionClick: (Session) -> Unit,
    onFavouriteClick: (Session) -> Boolean,
) {
    var currentSessionDate = sessions.first().date
    val groupedByDateSessions = mutableListOf<List<Session>>()
    
    var matchingDateSessions = mutableListOf<Session>()
    sessions.forEach {
        if (currentSessionDate == it.date) {
            matchingDateSessions += it
        } else {
            groupedByDateSessions += matchingDateSessions
            matchingDateSessions = mutableListOf()
            matchingDateSessions += it
        }
        currentSessionDate = it.date
    }
    
    items(groupedByDateSessions) {
        Column {
            Text(
                text = it.first().date,
                modifier = Modifier.padding(top = 14.dp),
                color = LocalColorScheme.current.secondaryTextColor
            )
            it.forEach { session ->
                Session(session, onUnsuccessfulAdd, onFavouriteClick, onSessionClick)
            }
        }
    }
}

@Composable
private fun Session(
    session: Session,
    onUnsuccessfulAdd: () -> Unit,
    onFavouriteClick: (Session) -> Boolean,
    onSessionClick: (Session) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColorScheme.current.cardContainerColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSessionClick(session) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        ) {
            AsyncImage(
                model = session.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )
            Column {
                Text(
                    text = session.speaker,
                    modifier = Modifier.width(200.dp),
                    fontWeight = FontWeight.Bold,
                    color = LocalColorScheme.current.primaryTextColor
                )
                Text(
                    text = session.timeInterval,
                    fontWeight = FontWeight.Bold,
                    color = LocalColorScheme.current.primaryTextColor
                )
                Text(
                    text = session.description,
                    modifier = Modifier.width(200.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = LocalColorScheme.current.primaryTextColor
                )
            }
            
            var isFavourite by remember { mutableStateOf(session.isFavourite) }
            val iconId = if (isFavourite) R.drawable.ic_filled_heart else R.drawable.ic_outlined_heart
            
            IconButton(onClick = {
                isFavourite = !isFavourite
                val isUnsuccessful = !onFavouriteClick(session.copy(isFavourite = isFavourite))
                if (isUnsuccessful) {
                    isFavourite = !isFavourite
                    onUnsuccessfulAdd()
                }
            }) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = if (isFavourite) LocalColorScheme.current.filledHeartIconTint else LocalColorScheme.current.outlinedHeartIconTint
                )
            }
        }
    }
}

private fun LazyListScope.errorState(error: SessionsError, onTryAgainClick: () -> Unit) {
    when (error) {
        is SessionsError.NoInternet -> item { NoInternetError(onTryAgainClick) }
    }
}

@Composable
private fun NoInternetError(onTryAgainClick: () -> Unit) {
    Text(text = stringResource(id = R.string.error_no_internet))
    
    Button(onClick = onTryAgainClick) {
        Text(text = stringResource(id = R.string.try_again))
    }
}

fun LazyListScope.loadingState() {
    item {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
