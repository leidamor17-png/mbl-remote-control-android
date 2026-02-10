package com.manus.mblremote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manus.mblremote.data.PreferencesDataStoreImpl
import com.manus.mblremote.model.ConnectionState
import com.manus.mblremote.network.SocketClient
import com.manus.mblremote.repository.ConnectionRepository
import com.manus.mblremote.ui.screens.*
import com.manus.mblremote.ui.theme.MBLRemoteTheme
import com.manus.mblremote.viewmodel.ConnectionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBLRemoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MBLRemoteApp()
                }
            }
        }
    }
}

@Composable
fun MBLRemoteApp() {
    val navController = rememberNavController()

    // ✅ CONTEXTO FORA DO remember
    val context = LocalContext.current

    // ✅ Objetos estáveis
    val socketClient = remember { SocketClient() }
    val dataStore = remember { PreferencesDataStoreImpl(context) }
    val repository = remember { ConnectionRepository(socketClient, dataStore) }

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("connection") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("connection") {
            ConnectionScreenContainer(
                navController = navController,
                repository = repository
            )
        }

        composable("control") {
            ControlScreenContainer(
                navController = navController,
                repository = repository
            )
        }
    }
}

@Composable
fun ConnectionScreenContainer(
    navController: NavHostController,
    repository: ConnectionRepository
) {
    val viewModel: ConnectionViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ConnectionViewModel(repository) as T
            }
        }
    )

    val connectionState by viewModel.connectionState.collectAsState()
    val recentDevices by viewModel.recentDevices.collectAsState()

    ConnectionScreen(
        connectionState = connectionState,
        recentDevices = recentDevices,
        onConnect = { ip, port ->
            viewModel.connect(ip, port)
        },
        onSelectRecent = { device ->
            viewModel.connect(device.ip, device.port)
        }
    )

    // ✅ navegação reagindo ao estado
    LaunchedEffect(connectionState) {
        if (connectionState is ConnectionState.Connected) {
            navController.navigate("control") {
                popUpTo("connection") { inclusive = true }
            }
        }
    }
}

@Composable
fun ControlScreenContainer(
    navController: NavHostController,
    repository: ConnectionRepository
) {
    val viewModel: ConnectionViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ConnectionViewModel(repository) as T
            }
        }
    )

    val connectionState by viewModel.connectionState.collectAsState()

    ControlScreen(
        connectionState = connectionState,
        onDirectionalPress = { /* TODO */ },
        onVolumeUp = { /* TODO */ },
        onVolumeDown = { /* TODO */ },
        onBack = { /* TODO */ },
        onHome = { /* TODO */ },
        onDisconnect = {
            viewModel.disconnect()
            navController.navigate("connection") {
                popUpTo("control") { inclusive = true }
            }
        }
    )
}
