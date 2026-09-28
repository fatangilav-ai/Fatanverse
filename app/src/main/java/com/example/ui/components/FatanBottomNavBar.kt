package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.ui.AppScreen
import com.example.ui.theme.CyanCore
import com.example.ui.theme.SlateDark800
import com.example.ui.theme.SlateDark900
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.TextPrimaryDark

data class NavItem(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun FatanBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    val items = listOf(
        NavItem(AppScreen.HOME, "Cerita", Icons.Default.Book, "nav_story"),
        NavItem(AppScreen.CHARACTERS, "Karakter", Icons.Default.People, "nav_characters"),
        NavItem(AppScreen.VS_BATTLE_WIKI, "VS Battle", Icons.Default.SportsKabaddi, "nav_vs_battle"),
        NavItem(AppScreen.INTERACTIVE_TERMINAL, "Terminal", Icons.Default.Engineering, "nav_terminal"),
        NavItem(AppScreen.FUN_FACTS, "Fun Fact", Icons.Default.Lightbulb, "nav_fun_fact")
    )

    // Determine selected item
    val selectedScreen = when (currentScreen) {
        AppScreen.HOME, AppScreen.READER -> AppScreen.HOME
        AppScreen.CHARACTERS, AppScreen.CHARACTER_DETAIL -> AppScreen.CHARACTERS
        AppScreen.VS_BATTLE_WIKI, AppScreen.VS_SIMULATOR -> AppScreen.VS_BATTLE_WIKI
        AppScreen.INTERACTIVE_TERMINAL -> AppScreen.INTERACTIVE_TERMINAL
        AppScreen.FUN_FACTS -> AppScreen.FUN_FACTS
        AppScreen.NOTIFICATIONS_SETTINGS -> AppScreen.HOME
    }

    NavigationBar(
        containerColor = SlateDark900,
        contentColor = TextPrimaryDark
    ) {
        items.forEach { item ->
            val isSelected = selectedScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) CyanCore else SlateMuted
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) CyanCore else SlateMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = SlateDark800
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
