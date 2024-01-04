package com.example.livescoreapp.repository

import com.example.livescoreapp.data.remote.models.ElenaApiService
import com.example.livescoreapp.data.remote.models.Match
import javax.inject.Inject

class InplayMatchesRepository @Inject constructor(private val elenaApiService: ElenaApiService) {
    suspend fun getAllInPlayMatches(): List<Match> = elenaApiService.getInplayMatches().result

}