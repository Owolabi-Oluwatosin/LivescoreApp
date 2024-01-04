package com.example.livescoreapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.livescoreapp.data.remote.models.Match
import com.example.livescoreapp.ui.theme.LiveScoreAppTheme
import com.example.livescoreapp.viewmodel.state.InplayMatchesViewModel
import com.example.livescoreapp.viewmodel.state.MatchesState
import com.example.livescoreapp.viewmodel.state.UpcomingMatchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.black)
        setContent {
            LiveScoreAppTheme {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    TopNavBar()
                    FetchData()
                }
            }
        }
    }
}


@Composable
fun FetchData(inplayMatchesViewModel: InplayMatchesViewModel = viewModel(), upcomingMatchesViewModel: UpcomingMatchesViewModel = viewModel()) {
    Column {
        when (val state = inplayMatchesViewModel.inplayMatchesState.collectAsState().value) {
            is MatchesState.Empty -> Text(text = "No data available")
            is MatchesState.Loading -> Text(text = "Loading...")
            is MatchesState.Success -> LiveMatches(liveMatches = state.result)
            is MatchesState.Error -> Text(text = state.message)
        }

        when (val state = upcomingMatchesViewModel.upcomingMatchesState.collectAsState().value) {
            is MatchesState.Empty -> Text(text = "No data available")
            is MatchesState.Loading -> Text(text = "Loading...")
            is MatchesState.Success -> UpcomingMatches(upcomingMatches = state.result)
            is MatchesState.Error -> Text(text = state.message)
        }
    }
}

@Composable
fun UpcomingMatches(upcomingMatches: List<Match>) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = "Scheduled Matches",
            fontSize = 26.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        if (upcomingMatches.isEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "No Upcoming Matches Currently"
                )
                Text(
                    text = "No Upcoming Matches Currently",
                    style = MaterialTheme.typography.h6,
                )
            }
        }else{
            LazyColumn(
                modifier = Modifier.padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(upcomingMatches.size){
                    UpcomingMatchItem(match = upcomingMatches[it])
                }
            }
        }

    }
}


@Composable
fun UpcomingMatchItem(match: Match){
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(bottom = 10.dp)
    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val month = getMatchDayAndMonth(match.event_date)
                val time = getMatchTime(match.event_time)
                Text(
                    text = match.event_away_team,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "$time\n$month",
                    fontSize = 18.sp,
                    color = Color.Green,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = match.event_home_team,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
    }
}

@Composable
fun getMatchDayAndMonth(date: String): String? {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val formatter = SimpleDateFormat("d MMM", Locale.ENGLISH)
    return date.let { it -> parser.parse(it)?.let { formatter.format(it) } }
}

@Composable
fun getMatchTime(time: String): String? {
    val parser = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    return time.let { it -> parser.parse(it)?.let { formatter.format(it) } }
}



@Composable
fun LiveMatches(liveMatches: List<Match>) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = "Live Matches",
            fontSize = 26.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        if (liveMatches.isEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "No Live Matches Currently"
                )
                Text(
                    text = "No Live Matches Currently",
                    style = MaterialTheme.typography.h6,
                )
            }
        }else{
            LazyRow(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(liveMatches.size){
                    LiveMatchItem(match = liveMatches[it])
                }
            }
        }

    }
}

@Composable
fun LiveMatchItem(match: Match){
//    Log.i("Result", "$match")
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .width(300.dp)
            .height(150.dp)
    ) {
       Column(modifier = Modifier.padding(10.dp)) {
           Text(
               text = match.league_name,
               modifier = Modifier.align(Alignment.CenterHorizontally),
               style = MaterialTheme.typography.h5,
           )
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(10.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(
                   text = match.event_away_team,
                   fontSize = 16.sp,
                   modifier = Modifier.padding(top = 10.dp)
               )
               Text(
                   text = match.event_halftime_result,
                   fontSize = 16.sp,
                   modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)
               )
               Text(
                   text = match.event_home_team,
                   fontSize = 16.sp,
                   modifier = Modifier.padding(top = 10.dp)
               )
           }
           Text(
               text = match.event_status,
               fontSize = 18.sp,
               color = Color.Green,
               textAlign = TextAlign.Center,
               modifier = Modifier.align(Alignment.CenterHorizontally)
           )
       }
    }
}


@Composable
fun TopNavBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh Icon")
        }

        Text(
            text = "LiveScores",
            color = Color.Black,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium
        )

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.modeicon),
                contentDescription = "Toggle Theme"
            )
        }
    }
}