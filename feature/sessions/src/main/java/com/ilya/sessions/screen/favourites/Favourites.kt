package com.ilya.sessions.screen.favourites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ilya.core.isFirst
import com.ilya.core.isLast
import com.ilya.data.retrofit.Session
import com.ilya.sessions.R
import com.ilya.theme.LocalColorScheme

fun LazyListScope.favourites(favouriteList: List<Session>, onSessionClick: (Int) -> Unit) {
    item {
        LazyRow(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .height(150.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (favouriteList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_favorites),
                            color = LocalColorScheme.current.secondaryTextColor,
                            fontSize = 20.sp
                        )
                    }
                }
            } else {
                items(favouriteList) {
                    val modifier = when (favouriteList.size) {
                        1 -> Modifier.padding(horizontal = 20.dp)
                        2 -> when {
                            favouriteList.isFirst(it) -> Modifier.padding(start = 20.dp)
                            favouriteList.isLast(it) -> Modifier.padding(horizontal = 20.dp)
                            else -> Modifier
                        }
                        
                        else -> when {
                            favouriteList.isFirst(it) -> Modifier.padding(start = 20.dp)
                            favouriteList.isLast(it) -> Modifier.padding(end = 20.dp)
                            else -> Modifier.padding(horizontal = 20.dp)
                        }
                    }
                    
                    Favorite(it, onSessionClick, modifier)
                }
            }
        }
    }
}

@Composable
fun Favorite(session: Session, onFavouriteClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .size(150.dp)
            .clickable { onFavouriteClick(session.id.toInt()) },
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