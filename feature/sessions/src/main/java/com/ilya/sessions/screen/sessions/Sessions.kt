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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.ilya.sessions.models.GroupedSessions
import com.ilya.sessions.screen.SessionsScreenState
import com.ilya.theme.LocalColorScheme
import com.ilya.theme.LocalTypography

fun LazyListScope.sessions(
    sessionsState: SessionsScreenState,
    onFavouriteClick: (Session) -> Unit,
    onSessionClick: (Int) -> Unit,
    onTryAgainClick: () -> Unit,
) {
    when (sessionsState) {
        is SessionsScreenState.Loading -> loadingState()
        is SessionsScreenState.Error -> errorState(sessionsState.error, onTryAgainClick)
        is SessionsScreenState.ShowSessions -> showSessionState(
            sessionsState.sessions,
            onFavouriteClick,
            onSessionClick
        )
        
        is SessionsScreenState.ShowSearchedSessions -> showSessionState(
            sessionsState.sessions,
            onFavouriteClick,
            onSessionClick
        )
    }
}

@Composable
private fun Session(
    session: Session,
    onFavouriteClick: (Session) -> Unit,
    onSessionClick: (Int) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColorScheme.current.cardContainerColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSessionClick(session.id.toInt()) }
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
            Column(
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
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
            
            IconButton(
                onClick = {
                    onFavouriteClick(session)
                }
            ) {
                Icon(
                    painter = painterResource(id = session.iconId()),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = session.tint()
                )
            }
        }
    }
}

@Composable
private fun Session.tint(): Color {
    return if (isFavourite) LocalColorScheme.current.filledHeartIconTint else LocalColorScheme.current.outlinedHeartIconTint
}

private fun Session.iconId(): Int {
    return if (isFavourite) R.drawable.ic_filled_heart else R.drawable.ic_outlined_heart
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

private fun LazyListScope.loadingState() {
    item {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

private fun LazyListScope.showSessionState(
    sessions: List<GroupedSessions>,
    onFavouriteClick: (Session) -> Unit,
    onSessionClick: (Int) -> Unit,
) {
    items(sessions) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text = it.date,
                color = LocalColorScheme.current.secondaryTextColor,
                fontSize = LocalTypography.current.tinyFontSize,
                modifier = Modifier.padding(top = 16.dp)
            )
            it.sessions.forEach {
                Session(it, onFavouriteClick, onSessionClick)
            }
        }
    }
}
