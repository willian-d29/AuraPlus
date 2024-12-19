package com.d29.auraplus.view.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.d29.auraplus.viewmodel.AuthViewModel
import com.d29.auraplus.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val emotions by homeViewModel.allEmotions.collectAsState(initial = emptyList())
    val currentDate = getCurrentMonthAndYear()
    val userName = authViewModel.currentUserName.collectAsState(initial = "Usuario").value

    // Fondo pastel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F8FF)) // Fondo azul pastel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bienvenida centrada
            Text(
                text = "Bienvenido, $userName 👋",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = Color(0xFF4CAF50), // Verde agradable
                textAlign = TextAlign.Center
            )

            // Mes y año
            Text(
                text = currentDate,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp
                ),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Lista de registros emocionales
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(emotions) { emotion ->
                    var showEditDialog by remember { mutableStateOf(false) }
                    var showDeleteDialog by remember { mutableStateOf(false) }

                    if (showEditDialog) {
                        EditEmotionDialog(
                            currentName = emotion.name,
                            currentNotes = emotion.notes,
                            onDismiss = { showEditDialog = false },
                            onSave = { newName, newNotes ->
                                homeViewModel.editEmotion(emotion.id, newName, newNotes)
                                showEditDialog = false
                            }
                        )
                    }

                    if (showDeleteDialog) {
                        DeleteEmotionDialog(
                            onConfirm = {
                                homeViewModel.deleteEmotion(emotion.id)
                                showDeleteDialog = false
                            },
                            onDismiss = { showDeleteDialog = false }
                        )
                    }

                    EmotionCard(
                        name = emotion.name,
                        notes = emotion.notes,
                        timestamp = emotion.timestamp,
                        onEdit = { showEditDialog = true },
                        onDelete = { showDeleteDialog = true }
                    )
                }
            }
        }
    }
}

@Composable
fun EmotionCard(
    name: String,
    notes: String,
    timestamp: Long,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val emotionColor = when (name) {
        "Excelente" -> Color(0xFF4CAF50)
        "Bien" -> Color(0xFF2196F3)
        "Regular" -> Color(0xFFFFEB3B)
        "Mal" -> Color(0xFFFF9800)
        "Terrible" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = emotionColor.copy(alpha = 0.1f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Emoción: $name", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
                Text("Fecha: $formattedDate", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp))
                if (notes.isNotBlank()) {
                    Text("Notas: $notes", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp))
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color(0xFF4CAF50))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun EditEmotionDialog(
    currentName: String,
    currentNotes: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var notes by remember { mutableStateOf(currentNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Emoción") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Emoción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name, notes) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DeleteEmotionDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar Emoción") },
        text = { Text("¿Estás seguro de que deseas eliminar esta emoción? Esta acción no se puede deshacer.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun getCurrentMonthAndYear(): String {
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return dateFormat.format(Date()).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}
