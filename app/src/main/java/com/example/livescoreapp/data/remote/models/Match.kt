package com.example.livescoreapp.data.remote.models

data class Match(
    val league_name: String,
    val home_team_key: Int,
    val event_home_team: String,
    val away_team_key: Int,
    val event_away_team: String,
    val event_date: String,
    val event_time: String,
    val event_status: String,
    val event_halftime_result: String,
    val event_final_result: String,
    val event_ft_result: String,
)
