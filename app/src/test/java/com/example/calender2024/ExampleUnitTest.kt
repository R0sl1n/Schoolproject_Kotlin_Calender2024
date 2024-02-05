package com.example.calender2024

import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*
import java.time.temporal.ChronoUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class CalendarUtilsTest {

    @Test
    fun calculateWorkdays_inMay2024_correctWorkdaysCount() {
        val expectedWorkdays = 23 // Expected work days in May 2024
        val actualWorkdays = calculateWorkdays(2024, 5) // May = 5
        assertEquals(expectedWorkdays, actualWorkdays)
    }

    @Test
    fun daysSinceJanFirst_fromJan1ToMay1_2024() {
        val startOfYear = LocalDate.of(2024, 1, 1)
        val dateToCheck = LocalDate.of(2024, 5, 1) // 1. mai 2024
        val expectedDays = 120 // Number of days from Jan 1. to May 1.
        val actualDays = ChronoUnit.DAYS.between(startOfYear, dateToCheck) -1
        assertEquals(expectedDays.toLong(), actualDays)
    }
}