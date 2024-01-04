package com.example.livescoreapp.repository

import com.example.livescoreapp.data.remote.models.ElenaApiService
import com.example.livescoreapp.data.remote.models.Match
import javax.inject.Inject

class UpcomingMatchesRepository @Inject constructor(private val elenaApiService: ElenaApiService) {
    suspend fun getAllUpcomingMatches(): List<Match> = elenaApiService.getUpcomingMatches().result

}