package com.apptasya.moovy.presentation.genres

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.apptasya.moovy.R
import com.apptasya.moovy.presentation.MoovyNavigator
import com.apptasya.moovy.presentation.genres.model.GenreUiElement
import com.apptasya.moovy.presentation.theme.LocalColors
import com.apptasya.moovy.presentation.theme.LocalDimensions
import com.apptasya.moovy.presentation.ui.ProgressBar
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Destination
@Composable
fun GenresScreen(
    navigator: MoovyNavigator,
    viewModel: GenresViewModel = hiltViewModel()
) {
    //TODO: do some ui improvements
    val colors = LocalColors.current
    val dimensions = LocalDimensions.current

    HandleUiEffects(viewModel.uiEffect, navigator)

    val uiState = viewModel.uiState
    val genres = uiState.genres

    Surface(
        color = colors.colorSurface,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = dimensions.space3Xl)
        ) {
            items(genres.size) {
                val genre = genres[it]
                GenreItem(genre) {
                    viewModel.onGenrePress(genre)
                }
            }
        }

        if (uiState.isShowingSaveGenres) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = {
                    viewModel.setGenres()
                }) {
                    Text(text = "Save Genres")
                }
            }
        }

        ProgressBar(uiState.isLoading)
    }
}

@Composable
fun GenreItem(genre: GenreUiElement, onPress: () -> Unit) {
    val colors = LocalColors.current
    val dimensions = LocalDimensions.current

    Surface(
        color = colors.colorSecondary,
        modifier = Modifier.padding(dimensions.spaceSm)
    ) {
        val borderWidth = if (genre.isSelected) 2.dp else 0.dp
        ConstraintLayout(
            modifier = Modifier
                .border(width = borderWidth, color = colors.colorPrimary)
                .clickable { onPress() }
        ) {
            val (imageRef, selectedRef, labelRef) = createRefs()

            AsyncImage(
                model = genre.photo,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_genre_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .aspectRatio(1.0f)
                    .constrainAs(imageRef) {
                        width = Dimension.fillToConstraints
                        start.linkTo(parent.start, dimensions.spaceSm)
                        end.linkTo(parent.end, dimensions.spaceSm)
                        top.linkTo(parent.top, dimensions.spaceSm)
                    }
            )

            Text(
                text = genre.name,
                modifier = Modifier.constrainAs(labelRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(imageRef.bottom, dimensions.spaceSm)
                    bottom.linkTo(parent.bottom, dimensions.spaceSm)
                }
            )

            if (genre.isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_selected),
                    contentDescription = null,
                    tint = colors.colorPrimary,
                    modifier = Modifier
                        .size(dimensions.spaceXxl)
                        .constrainAs(selectedRef) {
                            top.linkTo(imageRef.top, dimensions.spaceSm)
                            end.linkTo(parent.end, dimensions.spaceSm)
                        }
                )
            }
        }
    }
}


@Composable
private fun HandleUiEffects(uiEffect: SharedFlow<GenresEffect>, navigator: MoovyNavigator) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch {
                uiEffect.collect { uiEffect ->
                    when (uiEffect) {
                        is GenresEffect.ErrorEffect -> {
                            //TODO: show snack bar
                        }
                        GenresEffect.SetGenresSuccessEffect -> {
                            navigator.navigateToMovies()
                        }
                    }
                }
            }
        }
    }
}