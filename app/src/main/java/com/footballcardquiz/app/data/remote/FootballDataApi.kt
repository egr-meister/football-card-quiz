package com.footballcardquiz.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * football-data.org API v4 — matches endpoint only.
 * The X-Auth-Token header is injected by an OkHttp interceptor, not here,
 * so the token never appears in logs or source.
 */
interface FootballDataApi {

    @GET("matches")
    suspend fun getMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchesResponseDto
}
