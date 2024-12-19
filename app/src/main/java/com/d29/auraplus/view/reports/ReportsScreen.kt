package com.d29.auraplus.view.reports

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.d29.auraplus.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportsScreen(viewModel: HomeViewModel) {
    val currentDate = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(currentDate.get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(currentDate.get(Calendar.YEAR)) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf(0) }

    val stats by viewModel.getMonthlyStatistics(selectedMonth, selectedYear)
        .collectAsState(initial = emptyMap())


    val statsInt: Map<Int, String> = stats.mapKeys { it.key.toIntOrNull() ?: 0 }
        .mapValues { it.value.toString() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Fondo azul pastel
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado estilizado
            Text(
                "Calendario de Emociones",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Mes y Año
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (selectedMonth > 0) selectedMonth -= 1 else {
                        selectedMonth = 11
                        selectedYear -= 1
                    }
                }) {
                    Icon(Icons.Default.ArrowBackIos, contentDescription = "Mes Anterior", tint = Color(0xFF42A5F5))
                }
                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(
                        Calendar.getInstance().apply {
                            set(Calendar.MONTH, selectedMonth)
                            set(Calendar.YEAR, selectedYear)
                        }.time
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1976D2)
                )
                IconButton(onClick = {
                    if (selectedMonth < 11) selectedMonth += 1 else {
                        selectedMonth = 0
                        selectedYear += 1
                    }
                }) {
                    Icon(Icons.Default.ArrowForwardIos, contentDescription = "Mes Siguiente", tint = Color(0xFF42A5F5))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendario
            CalendarView(stats = statsInt, month = selectedMonth, year = selectedYear) { day ->
                selectedDay = day
                showDialog = true
            }

            if (showDialog) {
                EmotionDialog(
                    onDismiss = { showDialog = false },
                    onSave = { emotion, notes ->
                        viewModel.addEmotion(name = emotion, notes = notes)
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarView(stats: Map<Int, String>, month: Int, year: Int, onDayClick: (Int) -> Unit) {
    val daysInMonth = Calendar.getInstance().apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_MONTH, 1)
    }.getActualMaximum(Calendar.DAY_OF_MONTH)

    val firstDayOfWeek = Calendar.getInstance().apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_MONTH, 1)
    }.get(Calendar.DAY_OF_WEEK)

    Column {
        // Días de la semana
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach {
                Text(it, color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Días del calendario
        var day = 1
        while (day <= daysInMonth) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 1..7) {
                    if (day > daysInMonth || (day == 1 && i < firstDayOfWeek)) {
                        Box(modifier = Modifier.size(48.dp))
                    } else {
                        val emotion = stats[day]
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (emotion != null) Color(0xFFBBDEFB) else Color.LightGray, CircleShape)
                                .clickable { onDayClick(day) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (emotion != null) {
                                Text(emotion.first().toString(), color = Color(0xFF1E88E5))
                            } else {
                                Text(day.toString())
                            }
                        }
                        day++
                    }
                }
            }
        }
    }
}

@Composable
fun EmotionDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var emotion by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Emoción") },
        text = {
            Column {
                OutlinedTextField(value = emotion, onValueChange = { emotion = it }, label = { Text("Emoción") })
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notas (opcional)") })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(emotion, notes) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
