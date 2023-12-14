package com.ilya.sessions.screen.favourites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilya.data.retrofit.Session
import com.ilya.theme.LocalColorScheme

@Composable
fun Favourites(favouriteList: List<Session>, onSessionClick: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(vertical = 20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favouriteList) {
            Favourite(it, onSessionClick)
        }
    }
}

@Composable
fun Favourite(session: Session, onFavouriteClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .size(150.dp)
            .clickable { onFavouriteClick(session.id) },
        colors = CardDefaults.cardColors(containerColor = LocalColorScheme.current.cardContainerColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
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