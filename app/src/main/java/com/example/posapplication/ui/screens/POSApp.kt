package com.example.posapplication.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun POSApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Sales") },
                    label = { Text("Sales") },
                    selected = false,
                    onClick = { navController.navigate("sales") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Inventory") },
                    label = { Text("Inventory") },
                    selected = false,
                    onClick = { navController.navigate("inventory") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "sales",
            modifier = Modifier.padding(padding)
        ) {
            composable("sales") { SalesScreen() }
            composable("inventory") { InventoryScreen() }
        }
    }
}