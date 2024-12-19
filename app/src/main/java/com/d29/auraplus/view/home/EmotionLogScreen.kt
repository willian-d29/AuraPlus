package com.d29.auraplus.view.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.d29.auraplus.viewmodel.HomeViewModel

@Composable
fun EmotionLogScreen(viewModel: HomeViewModel, navController: NavController) {
    var selectedEmotion by remember { mutableStateOf<EmotionOption?>(null) }
    var notes by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9)) // Fondo pastel suave
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Text(
                "¿Cómo has estado?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF424242),
            )

            // Selección de emociones
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EmotionOption.entries.forEach { emotion ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        EmotionIcon(
                            emotion = emotion,
                            isSelected = selectedEmotion == emotion
                        ) {
                            selectedEmotion = emotion
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = emotion.label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF616161)
                        )
                    }
                }
            }

            // Campo para notas
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Escribe una nota ") },
                singleLine = false,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Guardar con Icono
            Button(
                onClick = {
                    selectedEmotion?.let {
                        viewModel.addEmotion(
                            name = it.name,
                            notes = notes
                        )
                        navController.navigate("home")
                    }
                },
                enabled = selectedEmotion != null,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Guardar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Composable para los íconos de emoción
@Composable
fun EmotionIcon(emotion: EmotionOption, isSelected: Boolean, onSelect: () -> Unit) {
    val backgroundColor = if (isSelected) emotion.color.copy(alpha = 0.3f) else Color.Transparent
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(2.dp, if (isSelected) emotion.color else Color.LightGray, CircleShape)
            .clickable { onSelect() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            emotion.icon,
            contentDescription = emotion.name,
            tint = emotion.color,
            modifier = Modifier.size(32.dp)
        )
    }
}

// Enum para definir las emociones
enum class EmotionOption(val icon: ImageVector, val color: Color, val label: String) {
    Excelente(Icons.Default.ThumbUp, Color(0xFF81C784), "Excelente"), // Verde pastel
    Bien(Icons.Default.SentimentSatisfied, Color(0xFF64B5F6), "Bien"), // Azul pastel
    Regular(Icons.Default.SentimentNeutral, Color(0xFFFFF176), "Regular"), // Amarillo pastel
    Mal(Icons.Default.SentimentDissatisfied, Color(0xFFFFB74D), "Mal"), // Naranja pastel
    Terrible(Icons.Default.SentimentVeryDissatisfied, Color(0xFFE57373), "Terrible") // Rojo pastel
}
