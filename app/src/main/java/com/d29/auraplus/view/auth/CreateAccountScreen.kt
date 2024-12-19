package com.d29.auraplus.view.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.d29.auraplus.viewmodel.AuthViewModel

@Composable
fun CreateAccountScreen(authViewModel: AuthViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authViewModel.authResult) {
        authViewModel.authResult.collect { success ->
            if (success == true) {
                successMessage = "Cuenta creada exitosamente. Puedes iniciar sesión."
            } else if (success == false) {
                errorMessage = authViewModel.currentUserName.value
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDFF6FF)) // Fondo azul pastel
    ) {



        // Contenido Principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono de volver
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
            }

            // Título
            Text(
                text = "Crear Cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campos de texto
            InputField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre",
                icon = Icons.Default.Person
            )
            InputField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                icon = Icons.Default.Email
            )
            InputField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                icon = Icons.Default.Lock
            )

            // Botón de registro
            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        authViewModel.register(name, email, password)
                        errorMessage = ""
                    } else {
                        errorMessage = "Todos los campos son obligatorios."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF407BFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar", fontSize = 18.sp, color = Color.White)
            }

            // Mensajes
            if (successMessage.isNotEmpty()) {
                Text(successMessage, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
            }
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = label, tint = Color(0xFF407BFF))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE3F2FD), // Azul pastel claro
            unfocusedContainerColor = Color(0xFFF1F8E9), // Verde pastel claro
            disabledContainerColor = Color(0xFFFFF3E0), // Naranja pastel claro
            focusedIndicatorColor = Color(0xFF81D4FA), // Azul indicador
            unfocusedIndicatorColor = Color(0xFFA5D6A7), // Verde indicador
            cursorColor = Color(0xFF80CBC4), // Turquesa pastel
            disabledIndicatorColor = Color(0xFFFFCCBC) // Naranja pastel claro
        )


    )
}
