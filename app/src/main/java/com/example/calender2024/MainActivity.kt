package com.example.calender2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calender2024.ui.theme.Calender2024Theme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calender2024Theme {
                // MaterialTheme to switch between light and dark mode.
                MaterialTheme(
                    colorScheme = if (isSystemInDarkTheme()) {
                        darkColorScheme()
                    } else {
                        lightColorScheme()
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Calender(Month.FEBRUARY, 2024)
                    }
                }
            }
        }
    }
}

//Function to calculate the number of workdays each month.
fun calculateWorkdays(year: Int, month: Int): Int {
    var workdays = 0
    val startDate = LocalDate.of(year, month, 1)
    val endDate = startDate.plusMonths(1).minusDays(1)
    val dayOfWeek = startDate.dayOfWeek.value // 1 = Monday, 7 = Sunday
    val offset = (dayOfWeek - 1) % 7

    var currentDate = startDate.minusDays(offset.toLong())
    while (currentDate.isBefore(endDate.plusDays(1))) {
        // Monday to Friday is defined as workdays.
        if (currentDate.dayOfWeek != DayOfWeek.SATURDAY && currentDate.dayOfWeek != DayOfWeek.SUNDAY) {
            workdays++
        }
        currentDate = currentDate.plusDays(1)
    }

    // Offset days are subtracted from the calculation to get the correct number of workdays.
    workdays -= offset

    return workdays
}

@Composable
fun Calender(month: Month, year: Int) {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)
    val daysInMonth = lastDayOfMonth.dayOfMonth
    val dayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 = Monday, 7 = Sunday

    // Adjust if your week starts on Sunday
    val offset = (dayOfWeek - 1) % 7
    val weekNumberFormatter = DateTimeFormatter.ofPattern("w")

    // Calculate workdays.
    val workdays = calculateWorkdays(year, month.value)

    var showDialogue by remember { mutableStateOf(false) }
    var chosenDay by remember { mutableStateOf(LocalDate.now()) } // Remembers the chosen date.
    val monthDisplayName = month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("no", "NO"))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("no", "NO")) else it.toString() }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(2.dp) // Reduced padding
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.solid else R.drawable.kalender_bakgrunn),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp), // Reduced padding
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$monthDisplayName $year",
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold)
                    )

                    val daysOfWeek = listOf(
                        stringResource(R.string.monday),
                        stringResource(R.string.tuesday),
                        stringResource(R.string.wednesday),
                        stringResource(R.string.thursday),
                        stringResource(R.string.friday),
                        stringResource(R.string.saturday),
                        stringResource(R.string.sunday)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Empty cell top left.
                        Box(
                            modifier = Modifier
                                .size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            /* Empty space top left*/
                        }

                        for (day in daysOfWeek) {
                            // Weekdays (Man, Tir, Ons, Tor, Fre, Lør, Søn)
                            Box(
                                modifier = Modifier
                                    .size(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    val weeksInMonth = ceil((daysInMonth + offset) / 7.0).toInt() // Updated calculation
                    for (week in 1..weeksInMonth) {
                        val firstDayOfWeek = firstDayOfMonth.plusDays(((week - 1) * 7 - offset).toLong())

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Week numbers
                            Box(
                                modifier = Modifier
                                    .size(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.uke,
                                        firstDayOfWeek.format(weekNumberFormatter)
                                    ),
                                    textAlign = TextAlign.Center,
                                )
                            }

                            for (day in 1..7) {
                                // Days of month
                                val currentDay = firstDayOfWeek.plusDays(day.toLong() - 1)
                                val isSelected = currentDay == chosenDay // Check if this is the chosen day.
                                Text(
                                    text = if (currentDay.isBefore(firstDayOfMonth) || currentDay.isAfter(
                                            lastDayOfMonth
                                        )
                                    ) {
                                        ""
                                    } else {
                                        currentDay.dayOfMonth.toString()
                                    },
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable {
                                            chosenDay =
                                                currentDay // Set currentDay to selected date
                                            showDialogue = true // Show dialogue
                                        }
                                        .padding(vertical = 12.dp)
                                        .then(
                                            if (isSelected) Modifier.background(
                                                Color.LightGray,
                                                RoundedCornerShape(4.dp)
                                            ) else Modifier
                                        ),
                                )
                            }
                        }
                    }

                    if (showDialogue) {
                        AlertDialog(
                            onDismissRequest = {
                                showDialogue = false
                            },
                            title = {
                                Text(stringResource(R.string.antall_dager_siden_1_januar))
                            },
                            text = {
                                Text(
                                    stringResource(
                                        R.string.dager, ChronoUnit.DAYS.between(
                                            LocalDate.of(year, 1, 1),
                                            chosenDay
                                        )
                                    )
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialogue = false
                                    }) {
                                    Text(stringResource(R.string.lukk))
                                }
                            }
                        )
                    }
                    Box {
                        Text(
                            text = stringResource(
                                R.string.antall_arbeidsdager_i,
                                month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("no", "NO")),
                                year,
                                workdays
                            ),
                            modifier = Modifier
                                .padding(top = 40.dp), // Add some space from the calendar to the text.
                            textAlign = TextAlign.Center,
                            style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                    Box {
                        Text(
                            text = stringResource(R.string.klikk_p_en_dag_for_finne_antall_arbeidsdager_siden_1_januar)
                            ,
                            modifier = Modifier
                                .padding(top = 20.dp),
                            textAlign = TextAlign.Center,
                            style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun CalenderPreview() {
    Calender2024Theme {
        Calender(Month.FEBRUARY, 2024)
    }
}
