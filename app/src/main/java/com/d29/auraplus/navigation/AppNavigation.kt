package com.d29.auraplus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.d29.auraplus.db.AppDatabase
import com.d29.auraplus.repository.EmotionLogRepository
import com.d29.auraplus.view.auth.CreateAccountScreen
import com.d29.auraplus.view.auth.LoginScreen
import com.d29.auraplus.view.home.EmotionLogScreen
import com.d29.auraplus.view.home.HomeScreen
import com.d29.auraplus.view.home.StatesScreen
import com.d29.auraplus.view.reports.ReportsScreen
import com.d29.auraplus.view.settings.SettingsScreen
import com.d29.auraplus.viewmodel.AuthViewModel
import com.d29.auraplus.viewmodel.HomeViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController: NavHostController = rememberNavController()

    // Inicializar base de datos y repositorio
    val db = AppDatabase.getInstance(context = LocalContext.current)
    val emotionLogRepository = EmotionLogRepository(db.emotionLogDao())

    // Instanciar HomeViewModel
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(emotionLogRepository)
    )

    // Estado para determinar si mostrar la barra inferior
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute !in listOf("login", "create_account")

    // Scaffold con barra de navegación inferior
    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(authViewModel = authViewModel, navController = navController)
            }
            composable("create_account") {
                CreateAccountScreen(authViewModel = authViewModel, navController = navController)
            }
            composable("home") {
                HomeScreen(homeViewModel = homeViewModel, authViewModel = authViewModel, navController = navController)
            }
            composable("emotionLog") {
                EmotionLogScreen(viewModel = homeViewModel, navController = navController)
            }
            composable("stats") {
                StatesScreen(viewModel = homeViewModel)

            }
            composable("settings") {
                SettingsScreen(navController = navController, authViewModel = authViewModel)
            }
            composable("reports") {
                ReportsScreen(viewModel = homeViewModel)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Inicio", Icons.Default.Home),
        BottomNavItem("stats", "Estadísticas", Icons.Default.Star),
        BottomNavItem("emotionLog", "Registrar", Icons.Default.Add),
        BottomNavItem("reports", "Calendario", Icons.Default.DateRange),
        BottomNavItem("settings", "Ajustes", Icons.Default.Settings),
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        style = TextStyle(fontSize = 10.sp) // Tamaño personalizado de la letra
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
