package com.d29.auraplus.view.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.d29.auraplus.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatesScreen(viewModel: HomeViewModel) {
    val currentDate = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(currentDate.get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(currentDate.get(Calendar.YEAR)) }
    val stats by viewModel.getMonthlyStatistics(selectedMonth, selectedYear)
        .collectAsState(initial = emptyMap())

    // Fondo Pastel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF6FF)) // Fondo azul pastel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título Centrado
            Text(
                "Estadísticas Mensuales",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            // Selector de Mes y Año con Flechas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (selectedMonth > 0) selectedMonth -= 1 else {
                        selectedMonth = 11
                        selectedYear -= 1
                    }
                }) { Icon(Icons.Filled.ArrowBackIos, contentDescription = "Mes Anterior") }

                Text(
                    text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(
                        Calendar.getInstance().apply {
                            set(Calendar.MONTH, selectedMonth)
                            set(Calendar.YEAR, selectedYear)
                        }.time
                    ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                IconButton(onClick = {
                    if (selectedMonth < 11) selectedMonth += 1 else {
                        selectedMonth = 0
                        selectedYear += 1
                    }
                }) { Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Mes Siguiente") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gráfico de Barras con Tarjeta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)) // Azul claro
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Filled.BarChart, contentDescription = "Gráfico de Barras", tint = Color.DarkGray)
                    Text(
                        "Gráfico de Barras",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    stats.forEach { (emotion, count) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = emotion,
                                modifier = Modifier.width(100.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = count.toFloat() / (stats.values.maxOrNull() ?: 1).toFloat())
                                    .height(20.dp)
                                    .background(getEmotionColor(emotion), shape = RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contador de Emociones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)) // Amarillo pastel
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Filled.List, contentDescription = "Contador de Emociones", tint = Color.DarkGray)
                    Text(
                        "Contador de Emociones",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    stats.forEach { (emotion, count) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(emotion, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text("$count días", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

fun getEmotionColor(emotion: String): Color {
    return when (emotion) {
        "Excelente" -> Color(0xFF4CAF50) // Verde
        "Bien" -> Color(0xFF2196F3) // Azul
        "Regular" -> Color(0xFFFFEB3B) // Amarillo
        "Mal" -> Color(0xFFFF9800) // Naranja
        "Terrible" -> Color(0xFFF44336) // Rojo
        else -> Color.Gray
    }
}
