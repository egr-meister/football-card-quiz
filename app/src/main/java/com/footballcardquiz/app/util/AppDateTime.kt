package com.footballcardquiz.app.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Simple, defensive date/time helpers. Everything is wrapped so that an
 * invalid input can never crash the app; safe fallbacks are returned instead.
 */
object AppDateTime {

    private const val DATE_PATTERN = "yyyy-MM-dd"
    private const val TIME_PATTERN = "HH:mm"

    private fun dateFormat(utc: Boolean = false) =
        SimpleDateFormat(DATE_PATTERN, Locale.US).apply {
            isLenient = false
            if (utc) timeZone = TimeZone.getTimeZone("UTC")
        }

    private fun timeFormat(utc: Boolean = false) =
        SimpleDateFormat(TIME_PATTERN, Locale.US).apply {
            isLenient = false
            if (utc) timeZone = TimeZone.getTimeZone("UTC")
        }

    /** Today's local date as YYYY-MM-DD. */
    fun today(): String = dateFormat().format(Date())

    /** Today's local date plus [days] as YYYY-MM-DD. */
    fun todayPlus(days: Int): String {
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, days) }
        return dateFormat().format(cal.time)
    }

    fun currentTime(): String = timeFormat().format(Date())

    /** Sortable timestamp string, e.g. 2026-07-09T14:03:22. */
    fun nowTimestamp(): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(Date())

    /** Human-friendly "last updated" label, e.g. 2026-07-09 14:03. */
    fun nowReadable(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())

    fun isValidDate(value: String): Boolean {
        if (value.isBlank()) return false
        return try {
            dateFormat().parse(value); true
        } catch (e: Exception) {
            false
        }
    }

    /** dateTo must not be earlier than dateFrom (both must be valid). */
    fun isRangeValid(from: String, to: String): Boolean {
        if (!isValidDate(from) || !isValidDate(to)) return false
        return try {
            val f = dateFormat().parse(from)
            val t = dateFormat().parse(to)
            f != null && t != null && !t.before(f)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Convert an ISO UTC datetime string (e.g. 2026-07-09T18:30:00Z) from the
     * API into a local date + time pair. Returns safe fallbacks on any failure.
     */
    fun parseUtcToLocal(utcDate: String?): Pair<String, String> {
        if (utcDate.isNullOrBlank()) return "" to ""
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm'Z'",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
        for (p in patterns) {
            try {
                val parser = SimpleDateFormat(p, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                    isLenient = false
                }
                val parsed = parser.parse(utcDate) ?: continue
                return dateFormat().format(parsed) to timeFormat().format(parsed)
            } catch (e: Exception) {
                // try next pattern
            }
        }
        // Last resort: take the date portion literally if it looks right.
        val datePart = utcDate.take(10)
        return if (isValidDate(datePart)) datePart to "" else "" to ""
    }
}
