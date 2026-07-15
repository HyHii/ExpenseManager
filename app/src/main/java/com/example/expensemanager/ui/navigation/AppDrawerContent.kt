package com.example.expensemanager.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

data class DrawerMenuItem(
    val title: String,
    val route: String
)

@Composable
fun AppDrawerContent(
    navController: NavHostController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mainItems = listOf(
        DrawerMenuItem("Dashboard", Screen.Dashboard.route),
        DrawerMenuItem("Transactions", Screen.Transactions.route),
        DrawerMenuItem("Scheduled", Screen.Scheduled.route),
        DrawerMenuItem("Analytics", Screen.Analytics.route),
        DrawerMenuItem("Goals", Screen.Goals.route)
    )

    val settingItems = listOf(
        DrawerMenuItem("Settings", Screen.Settings.route)
    )
    ModalDrawerSheet {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Expense Manager",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Personal finance app",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            mainItems.forEach { item ->
                NavigationDrawerItem(
                    label = {
                        Text(item.title)
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }

                        if (currentRoute != item.route) {
                            if (item.route == Screen.Dashboard.route) {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Dashboard.route) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Dashboard.route) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Settings",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            settingItems.forEach { item ->
                NavigationDrawerItem(
                    label = {
                        Text(item.title)
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }

                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}