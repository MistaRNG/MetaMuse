package com.example.metamuse.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.metamuse.R
import com.example.metamuse.ui.common.ErrorScreen
import com.example.metamuse.ui.details.DetailViewModel
import com.example.metamuse.ui.navigation.NavigationDestination

object SearchDestination : NavigationDestination {
    override val route = "search"
}

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    detailViewModel: DetailViewModel,
    navigateToDetails: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val museumObjects = searchViewModel.museUiState
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(R.string.search_snackbar_error)

    LaunchedEffect(searchViewModel.showNetworkErrorSnackbar) {
        if (searchViewModel.showNetworkErrorSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage, withDismissAction = true)
            searchViewModel.clearNetworkErrorEvent()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->

        if (searchViewModel.showInitialLoadError && museumObjects.isEmpty()) {
            ErrorScreen(onRetry = { searchViewModel.retryLastAction() })
            return@Scaffold
        }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = searchViewModel.searchQuery,
                onValueChange = { searchViewModel.onSearchQueryChange(it) },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                trailingIcon = {
                    if (searchViewModel.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchViewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.search_trailing_icon)
                            )
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(museumObjects) { obj ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                detailViewModel.museumObject = obj
                                navigateToDetails(obj.objectID)
                            },
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 2.dp,
                        shadowElevation = 4.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = obj.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ID: ${obj.objectID}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }

                if (searchViewModel.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            LaunchedEffect(listState, searchViewModel.searchMode) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                    .collect { visibleItems ->
                        val lastVisibleItem = visibleItems.lastOrNull()
                        val totalItemsCount = listState.layoutInfo.totalItemsCount
                        if (
                            lastVisibleItem != null &&
                            lastVisibleItem.index >= totalItemsCount - 5 &&
                            !searchViewModel.isLoading &&
                            !searchViewModel.searchMode
                        ) {
                            searchViewModel.loadMoreMuseumObjects()
                        }
                    }
            }
        }
    }
}

