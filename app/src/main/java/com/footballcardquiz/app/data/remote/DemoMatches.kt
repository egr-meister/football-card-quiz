package com.footballcardquiz.app.data.remote

import com.footballcardquiz.app.data.model.MatchSource
import com.footballcardquiz.app.data.model.NormalizedMatch
import com.footballcardquiz.app.util.AppDateTime

/**
 * Local demo match data used when no API token is configured, or as a fallback
 * when the API and cache are both unavailable. Team names are generic and
 * fictional — no real clubs, leagues, or logos are used.
 */
object DemoMatches {

    fun matches(): List<NormalizedMatch> {
        val d0 = AppDateTime.today()
        val d2 = AppDateTime.todayPlus(2)
        val d4 = AppDateTime.todayPlus(4)
        val d7 = AppDateTime.todayPlus(7)
        val d9 = AppDateTime.todayPlus(9)

        return listOf(
            NormalizedMatch(
                id = "demo-1", utcDate = "", date = d0, time = "18:00",
                competitionName = "Demo League", competitionCode = "DEMO",
                homeTeam = "Riverside Rovers", awayTeam = "Hilltop United",
                status = "SCHEDULED", source = MatchSource.Demo
            ),
            NormalizedMatch(
                id = "demo-2", utcDate = "", date = d0, time = "20:30",
                competitionName = "Demo League", competitionCode = "DEMO",
                homeTeam = "Coastal City", awayTeam = "Northgate Athletic",
                status = "SCHEDULED", source = MatchSource.Demo
            ),
            NormalizedMatch(
                id = "demo-3", utcDate = "", date = d2, time = "16:00",
                competitionName = "Demo Cup", competitionCode = "DCUP",
                homeTeam = "Meadow Park FC", awayTeam = "Old Town Wanderers",
                status = "SCHEDULED", source = MatchSource.Demo
            ),
            NormalizedMatch(
                id = "demo-4", utcDate = "", date = d4, time = "19:45",
                competitionName = "Demo League", competitionCode = "DEMO",
                homeTeam = "Sunfield Eagles", awayTeam = "Harbor Town",
                status = "SCHEDULED", source = MatchSource.Demo
            ),
            NormalizedMatch(
                id = "demo-5", utcDate = "", date = d7, time = "15:30",
                competitionName = "Demo Cup", competitionCode = "DCUP",
                homeTeam = "Greenvale Strikers", awayTeam = "Lakeside FC",
                status = "SCHEDULED", source = MatchSource.Demo
            ),
            NormalizedMatch(
                id = "demo-6", utcDate = "", date = d9, time = "17:00",
                competitionName = "Demo League", competitionCode = "DEMO",
                homeTeam = "Ironside Athletic", awayTeam = "Westbrook Rangers",
                status = "SCHEDULED", source = MatchSource.Demo
            )
        )
    }
}
