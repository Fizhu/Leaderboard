package com.fizhu.leaderboard.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.MILLISECOND
import java.util.concurrent.TimeUnit


/**
 * Created by fizhu on 05,June,2020
 * Hvyz.anbiya@gmail.com
 */
object DateTimeHelper {
    const val FORMAT_DATE_DMY = "dd-MM-yyyy"
    const val FORMAT_DATE_DMYHM = "dd-MM-yyyy HH:mm"
    const val FORMAT_DATE_DMYHM_WITH_SLASH = "dd/MM/yyyy HH:mm"
    const val FORMAT_DATE_DMYHMS = "dd-MM-yyyy HH:mm:ss"
    const val FORMAT_DATE_DMY_LONG_MONTH = "dd-MMMM-yyyy"
    const val FORMAT_DATE_DMY_LONG_MONTH_NO_SEPARATOR = "dd MMMM yyyy"
    const val FORMAT_DATE_DMY_SHORT_MONTH_NO_SEPARATOR = "dd MMM yyyy"
    const val FORMAT_DATE_DMY_SLASH = "dd/MMMM/yyyy"
    const val FORMAT_DATE_DMY_LONG_DAY = "dd MMMM yyyy"
    const val FORMAT_DATE_DMY_SHORT_SLASH = "dd/MM/yyyy"
    const val FORMAT_DATE_EDMY_LONG_MONTH = "EEEE, dd MMMM yyyy"
    const val FORMAT_DATE_EDMYHM_LONG_MONTH = "EEEE, dd MMMM yyyy HH:mm"
    const val FORMAT_DATE_EDMYHMS_LONG_MONTH = "EEEE, dd MMMM yyyy HH:mm:ss"
    const val FORMAT_DATE_YMD = "yyyy-MM-dd"
    const val FORMAT_DATE_YMD_NO_SEPARATOR = "yyyyMMdd"
    const val FORMAT_DATE_YMD_NO_SEPARATOR_SHORT = "yyMMdd"
    const val FORMAT_DATE_MDY_SLASH = "MM/dd/yyyy"
    const val FORMAT_DATE_YMD_SLASH = "yyyy/MM/dd"
    const val FORMAT_DATE_YMD_SEPARATOR = "yyyy-MM-dd"
    const val FORMAT_DATE_TIME_YMDHM = "yyyy-MM-dd HH:mm"
    const val FORMAT_DATE_TIME_YMDHMS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_DATE_TIME_WITH_DAY_NAME = "EEEE, dd/MM HH:mm"
    const val FORMAT_DATE_TIME_WITH_DAY_NAME_AND_MONTH_SHORT_NAME = "EEEE, dd MMM"
    const val FORMAT_DATE_TIME_YMDHMS_NO_SEPARATOR = "yyyyMMddHHmmss"
    const val FORMAT_DATE_TIME_DMYHM_LONG_MONTH = "dd-MMMM-yyyy HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_LONG_MONTH_NO_SEPARATOR = "dd MMMM yyyy HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_LONG_MONTH_NO_SEPARATOR_BUT_WITH_DOT = "dd MMMM yyyy . HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_LONG_WITH_STRIP = "dd MMM yyyy - HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_SHORT_MONTH_WITH_LINE = "dd MMM yyyy | HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_SHORT_MONTH_WITH_LINE_LONG_DAY = "EEEE, dd/MM/yyyy | HH:mm"
    const val FORMAT_DATE_TIME_DMYHM_SHORT_MONTH_WITH_COMMA = "dd MMM yyyy, HH:mm"
    const val FORMAT_DATE_TIME_DMYHMS_SHORT_MONTH_WITH_COMMA = "dd MMM yyyy, HH:mm:ss"
    const val FORMAT_TIME_HM = "HH:mm"
    const val FORMAT_TIME_HM_NO_SEPARATOR = "HHmm"
    fun getLastMidnight(): Date {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[MILLISECOND] = 0
        return cal.time
    }

    fun convertDate(date: String?, format: String?): String {
        var spf = SimpleDateFormat(FORMAT_DATE_YMD)
        val newDate = spf.parse(date)
        spf = SimpleDateFormat(format)
        return spf.format(newDate)
    }

    fun currentTime(): String {
        val current: Date = Calendar.getInstance().time
        val df = SimpleDateFormat(FORMAT_DATE_TIME_YMDHMS, Locale.getDefault())
        return df.format(current)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTime(dateValue: String?, format: String?): String {
        val inputPattern = FORMAT_DATE_TIME_YMDHMS
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(format)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(dateValue)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str!!
    }

    fun format(date: Date?, format: String?): String {
        val frm = SimpleDateFormat(format, Locale.getDefault())
        return frm.format(date)
    }

    @JvmOverloads
    fun parse(date: String?, format: String?, def: Date = Date()): Date {
        val frm = SimpleDateFormat(format, Locale.getDefault())
        return try {
            frm.parse(date)
        } catch (e: ParseException) {
            def
        }
    }

    @JvmOverloads
    fun parse(
        date: String?,
        format_awal: String?,
        format_akhir: String?,
        def: Date = Date()
    ): String {
        val _date = parse(date, format_awal, def)
        return format(_date, format_akhir)
    }

    fun diffDate(date_start: Date, date_end: Date): Long {
        val diffInMillies = date_end.time - date_start.time
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun diffMinutes(date_start: Date, date_end: Date): Long {
        val diffInMillies = date_end.time - date_start.time
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun diffHourMinutes(date_start: Date, date_end: Date): String {
        val diffInMillies = date_end.time - date_start.time
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillies)
        return String.format(
            Locale.getDefault(), "%d jam %d menit",
            hours, TimeUnit.MILLISECONDS.toMinutes(diffInMillies) - TimeUnit.HOURS.toMinutes(hours)
        )
    }

    /**
     *
     * Checks if two dates are on the same day ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is `null`
     */
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    /**
     *
     * Checks if two calendars represent the same day ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is `null`
     */
    fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "The dates must not be null" }
        return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }

    /**
     *
     * Checks if a date is today.
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isToday(date: Date?): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    /**
     *
     * Checks if a calendar date is today.
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isToday(cal: Calendar?): Boolean {
        return isSameDay(cal, Calendar.getInstance())
    }

    /**
     *
     * Checks if the first date is before the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isBeforeDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isBeforeDay(cal1, cal2)
    }

    /**
     *
     * Checks if the first calendar date is before the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun isBeforeDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "The dates must not be null" }
        if (cal1[Calendar.ERA] < cal2[Calendar.ERA]) return true
        if (cal1[Calendar.ERA] > cal2[Calendar.ERA]) return false
        if (cal1[Calendar.YEAR] < cal2[Calendar.YEAR]) return true
        return if (cal1[Calendar.YEAR] > cal2[Calendar.YEAR]) false else cal1[Calendar.DAY_OF_YEAR] < cal2[Calendar.DAY_OF_YEAR]
    }

    /**
     *
     * Checks if the first date is after the second date ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isAfterDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "The dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isAfterDay(cal1, cal2)
    }

    /**
     *
     * Checks if the first calendar date is after the second calendar date ignoring time.
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun isAfterDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "The dates must not be null" }
        if (cal1[Calendar.ERA] < cal2[Calendar.ERA]) return false
        if (cal1[Calendar.ERA] > cal2[Calendar.ERA]) return true
        if (cal1[Calendar.YEAR] < cal2[Calendar.YEAR]) return false
        return if (cal1[Calendar.YEAR] > cal2[Calendar.YEAR]) true else cal1[Calendar.DAY_OF_YEAR] > cal2[Calendar.DAY_OF_YEAR]
    }

    /**
     *
     * Checks if a date is after today and within a number of days in the future.
     *
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isWithinDaysFuture(date: Date?, days: Int): Boolean {
        requireNotNull(date) { "The date must not be null" }
        val cal = Calendar.getInstance()
        cal.time = date
        return isWithinDaysFuture(cal, days)
    }

    /**
     *
     * Checks if a calendar date is after today and within a number of days in the future.
     *
     * @param cal  the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isWithinDaysFuture(cal: Calendar?, days: Int): Boolean {
        requireNotNull(cal) { "The date must not be null" }
        val today = Calendar.getInstance()
        val future = Calendar.getInstance()
        future.add(Calendar.DAY_OF_YEAR, days)
        return isAfterDay(cal, today) && !isAfterDay(cal, future)
    }

    /**
     * Returns the given date with the time set to the start of the day.
     */
    fun getStart(date: Date?): Date? {
        return clearTime(date)
    }

    /**
     * Returns the given date with the time values cleared.
     */
    fun clearTime(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[MILLISECOND] = 0
        return c.time
    }
    /** Determines whether or not a date has any time values (hour, minute,
     * seconds or millisecondsReturns the given date with the time values cleared.  */
    /**
     * Determines whether or not a date has any time values.
     *
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    fun hasTime(date: Date?): Boolean {
        if (date == null) {
            return false
        }
        val c = Calendar.getInstance()
        c.time = date
        if (c[Calendar.HOUR_OF_DAY] > 0) {
            return true
        }
        if (c[Calendar.MINUTE] > 0) {
            return true
        }
        if (c[Calendar.SECOND] > 0) {
            return true
        }
        return c[MILLISECOND] > 0
    }

    /**
     * Returns the given date with time set to the end of the day
     */
    fun getEnd(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c[Calendar.HOUR_OF_DAY] = 23
        c[Calendar.MINUTE] = 59
        c[Calendar.SECOND] = 59
        c[MILLISECOND] = 999
        return c.time
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date.
     */
    fun max(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.after(d2)) d1 else d2
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date.
     */
    fun min(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.before(d2)) d1 else d2
    }

    /**
     * The maximum date possible.
     */
    var MAX_DATE = Date(Long.MAX_VALUE)
}