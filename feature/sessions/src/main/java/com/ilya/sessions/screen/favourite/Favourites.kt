package com.ilya.sessions.screen.favourite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilya.data.retrofit.Session
import com.ilya.sessions.R
import com.ilya.theme.LocalColorScheme


fun LazyListScope.favourites(favouritesState: List<Session>) {
    item {
        LazyRow {
            if (favouritesState.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_favourites),
                            color = LocalColorScheme.current.secondaryTextColor,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            items(favouritesState) {
                Favourite(it)
            }
        }
    }
}

@Composable
fun Favourite(session: Session) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColorScheme.current.cardContainerColor),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = session.timeInterval,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColorScheme.current.primaryTextColor
                )
                Text(text = session.date, color = LocalColorScheme.current.secondaryTextColor)
            }
            Column {
                Text(
                    text = session.speaker,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = LocalColorScheme.current.primaryTextColor
                )
                Text(
                    text = session.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = LocalColorScheme.current.secondaryTextColor
                )
            }
        }
    }
}