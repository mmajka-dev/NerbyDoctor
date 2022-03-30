package com.mmajka.nerbydoctor.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mmajka.nerbydoctor.ui.onboarding.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigateTo
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NavigationComponent(
    navigationEvent: SharedFlow<NavigationCommand>
) {
    val navController = rememberNavController()

    LaunchedEffect("navigation") {
        navigationEvent.onEach { command ->
            onCommand(command, navController)
        }.launchIn(this)
    }

    DestinationsNavHost(navGraph = NavGraphs.root)
}

private fun onCommand(
    command: NavigationCommand,
    navigator: NavController
) {
    when (command) {
        is NavigationCommand.Navigate -> navigator.navigateTo(
            command.destination,
            navOptionsBuilder = {
                command.popUpTo?.let { destination ->
                    popUpTo(destination) { inclusive = command.inclusive }
                }
            }
        )
        is NavigationCommand.PopUp -> navigator.popBackStack(
            command.destination.route,
            inclusive = command.inclusive
        )
        NavigationCommand.NavigateUp -> navigator.navigateUp()
    }
}