package com.example.metamuse.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.metamuse.R
import com.example.metamuse.data.model.MuseumObject
import com.example.metamuse.ui.navigation.NavigationDestination

object DetailsDestination : NavigationDestination {
    override val route = "details"
    const val idArg = "id"
    val fullRoute = "$route/{$idArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    museumObject: MuseumObject,
    navController: NavController
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.detail_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back_button),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hauptbild
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = museumObject.primaryImage.ifBlank { R.drawable.placeholder_img },
                    contentDescription = museumObject.title,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Galerie
            if (museumObject.additionalImages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Weitere Bilder",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )

                val pagerState = rememberPagerState(pageCount = { museumObject.additionalImages.size })

                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth()
                ) { page ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 2.dp,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = museumObject.additionalImages[page],
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                PagerIndicator(
                    currentPage = pagerState.currentPage,
                    pageCount = museumObject.additionalImages.size,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Titel und Künstler
            if (museumObject.title.isNotBlank()) {
                Text(
                    text = museumObject.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            if (museumObject.artistDisplayName.isNotBlank()) {
                Text(
                    text = museumObject.artistDisplayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
            }

            if (museumObject.artistDisplayBio.isNotBlank()) {
                Text(
                    text = museumObject.artistDisplayBio,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Metadaten
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (museumObject.objectDate.isNotBlank()) {
                    DetailRow(stringResource(R.string.details_creation), museumObject.objectDate)
                }
                if (museumObject.medium.isNotBlank()) {
                    DetailRow(stringResource(R.string.details_medium), museumObject.medium)
                }
                if (museumObject.dimensions.isNotBlank()) {
                    DetailRow(stringResource(R.string.details_dimensions), museumObject.dimensions)
                }
            }

            // Link
            if (museumObject.objectURL.isNotBlank()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.details_url_text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { uriHandler.openUri(museumObject.objectURL) }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun PagerIndicator(currentPage: Int, pageCount: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(8.dp)
                    .background(color, shape = RoundedCornerShape(50))
            )
        }
    }
}
