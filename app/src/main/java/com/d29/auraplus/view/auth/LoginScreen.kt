package com.d29.auraplus.view.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.d29.auraplus.viewmodel.AuthViewModel
import com.d29.auraplus.R

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val alphaAnimation = animateFloatAsState(if (!isLoading) 1f else 0.5f).value

    LaunchedEffect(authViewModel.authResult) {
        authViewModel.authResult.collect { success ->
            isLoading = false
            if (success == true) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                errorMessage = authViewModel.currentUserName.value
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Fondo azul pastel
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo en la parte superior
            Image(
                painter = painterResource(id = R.drawable.plus), // Reemplazar con tu logo
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp) // Tamaño del círculo
                    .clip(CircleShape) // Recorta la imagen en un círculo
                    .background(Color.White, CircleShape) // Fondo blanco en forma de círculo
                    .border(4.dp, Color(0xFF3F51B5), CircleShape) // Borde opcional
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto Login con animación
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically()
            ) {
                Text(
                    "Bienvenido a Aura +",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                    color = Color(0xFF3F51B5) // Color pastel
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Email Icon")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF3F51B5),
                    unfocusedIndicatorColor = Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alphaAnimation)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password Icon")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF3F51B5),
                    unfocusedIndicatorColor = Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alphaAnimation)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Login con animación de carga
            Button(
                onClick = {
                    isLoading = true
                    authViewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Iniciar Sesión", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate("create_account") }) {
                Text("¿No tienes cuenta? Regístrate aquí", color = Color(0xFF3F51B5))
            }

            // Error Message
            if (errorMessage.isNotEmpty()) {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
