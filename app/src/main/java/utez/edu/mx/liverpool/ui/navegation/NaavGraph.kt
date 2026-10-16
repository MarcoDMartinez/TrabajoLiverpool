package utez.edu.mx.liverpool.ui.navegation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import utez.edu.mx.liverpool.ui.screens.liverpool.LiverpoolScreen
import utez.edu.mx.liverpool.ui.screens.menu.MenuScreen

sealed class Route(val path: String) {
    object Menu : Route("menu")
    object Liverpool : Route("liverpool")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Menu.path,
        modifier = modifier
    ) {
        composable(Route.Menu.path) {
            MenuScreen(
                onNavigateToLiverpool = { navController.navigate(Route.Liverpool.path) }
            )
        }
        composable(Route.Liverpool.path) {
            LiverpoolScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
