package com.example.livescoreapp.viewmodel.state

import com.example.livescoreapp.data.remote.models.Match

sealed class MatchesState{
    object Empty: MatchesState()
    object Loading: MatchesState()
    class Success(val result: List<Match>): MatchesState()
    class Error(val message: String): MatchesState()
}
