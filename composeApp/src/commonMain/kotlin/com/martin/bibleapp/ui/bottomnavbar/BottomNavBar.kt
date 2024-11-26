package com.martin.bibleapp.ui.bottomnavbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.martin.bibleapp.ui.BibleScreen

data class BottomNavItem(
    val title: String,
    val navigationTarget: BibleScreen,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Bible", BibleScreen.BibleView(0), Icons.Default.Book),
    BottomNavItem("Comments", BibleScreen.BibleView(1), Icons.Default.Description),
    BottomNavItem("Search", BibleScreen.Search, Icons.Default.Search),
//    BottomNavItem("Settings", BibleScreen.Search, Icons.Default.Settings)
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                label = { Text(item.title) },
                selected = false,
                onClick = {
                    navController.navigate(item.navigationTarget)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}
