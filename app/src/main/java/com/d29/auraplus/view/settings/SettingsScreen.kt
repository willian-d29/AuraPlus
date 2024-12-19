package com.d29.auraplus.view.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.d29.auraplus.R
import com.d29.auraplus.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(navController: NavController, authViewModel: AuthViewModel) {
    val userName by authViewModel.currentUserName.collectAsState()
    val userEmail by authViewModel.currentUserEmail.collectAsState()

    // Imagen por defecto de perfil (hasta que se implemente selector)
    val placeholderImage = painterResource(id = R.drawable.iconer)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Fondo azul pastel
    ) {
        // Encabezado con imagen, nombre y correo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = placeholderImage,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { /* Abrir selector de imagen */ },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Text(
                    text = userEmail,
                    fontSize = 19.sp,
                    color = Color.Gray
                )
            }
        }

        // Opciones generales
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Opciones Generales",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OptionRow(Icons.Default.Brightness6, "Modo Oscuro")
            OptionRow(Icons.Default.Notifications, "Notificaciones")
            OptionRow(Icons.Default.Language, "Idioma")
            OptionRow(Icons.Default.Photo, "Galería de fotos")
            OptionRow(Icons.Default.Widgets, "Widgets")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Seguridad",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OptionRow(Icons.Default.Lock, "Cambiar contraseña")

            Spacer(modifier = Modifier.height(16.dp))

            // Cerrar sesión
            Button(
                onClick = {
                    authViewModel.logout() // Llamar a logout del AuthViewModel
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar sesión", color = Color.White)
                }
            }
        }

        // Versión de la app
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Versión 1.0.0",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun OptionRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Opciones futuras */ },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF2196F3))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
