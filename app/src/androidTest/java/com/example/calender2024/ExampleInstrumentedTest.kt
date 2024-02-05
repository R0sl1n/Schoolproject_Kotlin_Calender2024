package com.example.calender2024

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.calender2024.ui.theme.Calender2024Theme
import org.junit.Rule
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class CalendarDialogInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dialogShowsCorrectDaysSinceJanFirst() {
        // Static date for testing.
        val testDate = LocalDate.of(2024, 1, 15)
        val daysSinceYearStart = ChronoUnit.DAYS.between(LocalDate.of(testDate.year, 1, 1), testDate)

        composeTestRule.setContent {
            Calender2024Theme {
                Calender(testDate.month, testDate.year)
            }
        }

        // Presume the string to be 15th of January.
        val dayText = testDate.dayOfMonth.toString()

        // Execute click on the date.
        composeTestRule.onNodeWithText(dayText).performClick()

        // Build expected text.
        val expectedText = "$daysSinceYearStart dager"

        // Verify the expected text in the dialogue.
        composeTestRule.onNodeWithText(expectedText, useUnmergedTree = true).assertExists()
    }
}
