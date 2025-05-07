package com.example.metamuse.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.metamuse.container
import com.example.metamuse.model.MuseumObject
import com.example.metamuse.ui.details.DetailScreen
import com.example.metamuse.ui.details.DetailViewModel
import com.example.metamuse.ui.details.DetailsDestination
import com.example.metamuse.ui.search.SearchDestination
import com.example.metamuse.ui.search.SearchScreen
import com.example.metamuse.ui.search.SearchViewModel


val searchViewModel = SearchViewModel(container.metaMuseRepository)
val detailViewModel = DetailViewModel(container.metaMuseRepository)
/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MetaMuseNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SearchDestination.route,
        modifier = modifier
    ) {
        composable(route = SearchDestination.route) {
            SearchScreen(
                searchViewModel = searchViewModel,
                detailViewModel = detailViewModel,
                navigateToDetails = { id -> navController.navigate("details/$id") }
            )
        }
        composable(route = DetailsDestination.fullRoute) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(DetailsDestination.idArg)
            if (id != null) {
                DetailScreen(
                    museumObject = detailViewModel.museumObject!!,
                    navController = navController
                )
            } else {
                Text("Error: No ID found.")
            }
        }
    }
}
