package com.example.metamuse.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.metamuse.R
import com.example.metamuse.ui.details.DetailScreen
import com.example.metamuse.ui.details.DetailViewModel
import com.example.metamuse.ui.details.DetailsDestination
import com.example.metamuse.ui.search.SearchDestination
import com.example.metamuse.ui.search.SearchScreen
import com.example.metamuse.ui.search.SearchViewModel

@Composable
fun MetaMuseNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SearchDestination.route,
        modifier = modifier
    ) {
        composable(route = SearchDestination.route) {
            val searchViewModel: SearchViewModel = hiltViewModel()

            SearchScreen(
                searchViewModel = searchViewModel,
                navController = navController,
            )
        }

        composable(
            route = DetailsDestination.fullRoute,
            arguments = listOf(navArgument(DetailsDestination.idArg) { type = NavType.IntType })
        ) {
            val detailViewModel: DetailViewModel = hiltViewModel()
            val museumObject by detailViewModel.museumObject.collectAsState()

            if (museumObject != null) {
                DetailScreen(museumObject = museumObject!!, navController = navController)
            } else {
                Text(stringResource(R.string.error_no_id))
            }
        }
    }
}
