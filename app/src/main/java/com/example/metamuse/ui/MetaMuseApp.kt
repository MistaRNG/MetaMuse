package com.example.metamuse.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.metamuse.container
import com.example.metamuse.ui.navigation.MetaMuseNavHost
import com.example.metamuse.ui.search.SearchViewModel
import com.example.metamuse.ui.search.SearchScreen

@Composable
fun MetaMuseApp(navController: NavHostController = rememberNavController()) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MetaMuseNavHost(navController = navController)
        }
}