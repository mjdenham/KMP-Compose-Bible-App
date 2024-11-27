package com.martin.bibleapp.ui.bottomnavbar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.martin.bibleapp.resources.Res
import com.martin.bibleapp.resources.book_ribbon_24dp
import com.martin.bibleapp.resources.book_ribbon_24dp_filled
import com.martin.bibleapp.resources.search_24dp
import com.martin.bibleapp.resources.sticky_note_2_24dp
import com.martin.bibleapp.resources.sticky_note_2_24dp_filled
import com.martin.bibleapp.ui.BibleScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class BottomNavItem(
    val title: String,
    val navigationTarget: BibleScreen,
    val unselectedIcon: DrawableResource,
    val selectedIcon: DrawableResource
)

val bottomNavItems = listOf(
    BottomNavItem("Bible", BibleScreen.BibleView(0), Res.drawable.book_ribbon_24dp, Res.drawable.book_ribbon_24dp_filled),
    BottomNavItem("Comments", BibleScreen.BibleView(1), Res.drawable.sticky_note_2_24dp, Res.drawable.sticky_note_2_24dp_filled),
    BottomNavItem("Search", BibleScreen.Search, Res.drawable.search_24dp, Res.drawable.search_24dp),
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    NavigationBar {
        bottomNavItems.forEachIndexed { index, item ->
            val selected = index == selectedItemIndex
            NavigationBarItem(
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    navController.navigate(item.navigationTarget)
                    selectedItemIndex = index
                },
                icon = {
                    Icon(
                        painter = painterResource(if (selected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}
