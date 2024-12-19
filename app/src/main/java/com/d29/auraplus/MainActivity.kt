package com.d29.auraplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d29.auraplus.db.AppDatabase
import com.d29.auraplus.navigation.AppNavigation
import com.d29.auraplus.repository.UserRepository
import com.d29.auraplus.repository.EmotionLogRepository
import com.d29.auraplus.ui.theme.Practica5Theme
import com.d29.auraplus.viewmodel.AuthViewModel
import com.d29.auraplus.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos
        val db = AppDatabase.getInstance(applicationContext)

        // Crear repositorios
        val userRepository = UserRepository(db.userProfileDao())
        val emotionLogRepository = EmotionLogRepository(db.emotionLogDao())

        setContent {
            Practica5Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Inicializar AuthViewModel
                    val authViewModel: AuthViewModel = ViewModelProvider(
                        this,
                        AuthViewModel.Factory(userRepository)
                    )[AuthViewModel::class.java]

                    // Inicializar HomeViewModel (opcional para HomeScreen)
                    val homeViewModel: HomeViewModel = viewModel(
                        factory = HomeViewModel.Factory(emotionLogRepository)
                    )

                    // Llamada a la navegación
                    AppNavigation(authViewModel)
                }
            }
        }
    }
}
