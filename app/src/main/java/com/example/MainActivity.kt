package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.FatanViewModel
import com.example.ui.components.FatanBottomNavBar
import com.example.ui.components.FatanTopAppBar
import com.example.ui.screens.CharacterDetailScreen
import com.example.ui.screens.CharacterListScreen
import com.example.ui.screens.FunFactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InteractiveTerminalScreen
import com.example.ui.screens.NotificationSettingsScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.VsBattleScreen
import com.example.ui.theme.FatanverseTheme
import com.example.util.NotificationHelper

class MainActivity : ComponentActivity() {

    private var viewModelRef: FatanViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channel on app start
        NotificationHelper.createNotificationChannel(this)

        val targetChapterId = intent?.getStringExtra("TARGET_CHAPTER_ID")

        setContent {
            val fatanViewModel: FatanViewModel = viewModel()
            viewModelRef = fatanViewModel

            LaunchedEffect(targetChapterId) {
                if (!targetChapterId.isNullOrEmpty()) {
                    fatanViewModel.openChapter(targetChapterId)
                }
            }

            FatanverseApp(fatanViewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val target = intent.getStringExtra("TARGET_CHAPTER_ID")
        if (!target.isNullOrEmpty()) {
            viewModelRef?.openChapter(target)
        }
    }
}

@Composable
fun FatanverseApp(viewModel: FatanViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val activeChapter by viewModel.activeChapter.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            snackbarHostState.showSnackbar(toastMessage!!)
            viewModel.clearToast()
        }
    }

    val showBottomBar = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.CHARACTERS,
        AppScreen.VS_BATTLE_WIKI,
        AppScreen.VS_SIMULATOR,
        AppScreen.INTERACTIVE_TERMINAL,
        AppScreen.FUN_FACTS
    )

    val showTopBar = currentScreen !in listOf(AppScreen.READER)

    FatanverseTheme(darkTheme = true) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (showTopBar) {
                    FatanTopAppBar(
                        title = when (currentScreen) {
                            AppScreen.HOME -> "FATANVERSE"
                            AppScreen.CHARACTERS -> "Biodata Karakter"
                            AppScreen.CHARACTER_DETAIL -> "Profil Karakter"
                            AppScreen.VS_BATTLE_WIKI, AppScreen.VS_SIMULATOR -> "VS Battle Wiki"
                            AppScreen.INTERACTIVE_TERMINAL -> "Terminal Armor White Spark"
                            AppScreen.FUN_FACTS -> "Arsip Fun Fact"
                            AppScreen.NOTIFICATIONS_SETTINGS -> "Pengaturan & Notifikasi"
                            AppScreen.READER -> "Membaca Arc"
                        },
                        subtitle = when (currentScreen) {
                            AppScreen.HOME -> "Cerita Resmi & Arsip DHC"
                            AppScreen.CHARACTERS -> "Demon Hunter Corp"
                            AppScreen.VS_BATTLE_WIKI, AppScreen.VS_SIMULATOR -> "Power Scaling & Simulator"
                            AppScreen.INTERACTIVE_TERMINAL -> "Simulator Reaktor Inti"
                            AppScreen.FUN_FACTS -> "Rahasia Semesta"
                            else -> null
                        },
                        showBackButton = currentScreen in listOf(
                            AppScreen.CHARACTER_DETAIL,
                            AppScreen.NOTIFICATIONS_SETTINGS
                        ),
                        onBackClick = { viewModel.navigateBack() },
                        onNotificationClick = {
                            viewModel.navigateTo(AppScreen.NOTIFICATIONS_SETTINGS)
                        }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    FatanBottomNavBar(
                        currentScreen = currentScreen,
                        onNavigate = { screen -> viewModel.navigateTo(screen) }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.READER -> ReaderScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.INTERACTIVE_TERMINAL -> InteractiveTerminalScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.CHARACTERS -> CharacterListScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.CHARACTER_DETAIL -> CharacterDetailScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.VS_BATTLE_WIKI, AppScreen.VS_SIMULATOR -> VsBattleScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.FUN_FACTS -> FunFactScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                AppScreen.NOTIFICATIONS_SETTINGS -> NotificationSettingsScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
