package com.apptasya.moovy.presentation.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.apptasya.moovy.R
import com.apptasya.moovy.presentation.MoovyNavigator
import com.apptasya.moovy.presentation.movies.model.MovieUiElement
import com.apptasya.moovy.presentation.theme.LocalColors
import com.apptasya.moovy.presentation.theme.LocalDimensions
import com.apptasya.moovy.presentation.ui.ProgressBar
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@RootNavGraph(start = true)
@Destination
@Composable
fun MoviesScreen(
    navigator: MoovyNavigator, viewModel: MoviesViewModel = hiltViewModel()
) {
    HandleUiEffects(viewModel.uiEffect, navigator)
    val uiState = viewModel.uiState
    val movie = uiState.movies.getOrNull(0)

    val colors = LocalColors.current
    val dimensions = LocalDimensions.current

    Surface(
        color = colors.colorSurface, modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout {
            val (likesButtonRef, settingsButtonRef, movieInfoRef, likeMovieButtonRef, unlikeMovieButtonRef, spacerRef) = createRefs()

            IconButton(onClick = {
                navigator.navigateToLikes()
            }, modifier = Modifier
                .background(colors.colorPrimary, shape = CircleShape)
                .constrainAs(likesButtonRef) {
                    top.linkTo(parent.top, dimensions.spaceMd)
                    end.linkTo(parent.end, dimensions.spaceMd)
                }) {
                Icon(painter = painterResource(id = R.drawable.ic_like), contentDescription = null)
            }

            IconButton(onClick = {
                navigator.navigateToGenres(allowPopUp = true)
            }, modifier = Modifier
                .background(colors.colorPrimary, shape = CircleShape)
                .constrainAs(settingsButtonRef) {
                    top.linkTo(parent.top, dimensions.spaceMd)
                    end.linkTo(likesButtonRef.start, dimensions.spaceMd)
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_genres),
                    contentDescription = null
                )
            }

            if (movie != null) {
                IconButton(onClick = {
                    viewModel.likeMovie()
                }, modifier = Modifier
                    .background(colors.colorPrimary, shape = CircleShape)
                    .padding(dimensions.spaceMd)
                    .constrainAs(likeMovieButtonRef) {
                        bottom.linkTo(parent.bottom, dimensions.spaceLg)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_thumb_up),
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.spaceXxl)
                    )
                }

                Spacer(modifier = Modifier
                    .width(dimensions.spaceXl)
                    .constrainAs(spacerRef) {

                    })

                IconButton(onClick = {
                    viewModel.unlikeMovie()
                }, modifier = Modifier
                    .background(colors.colorPrimary, shape = CircleShape)
                    .padding(dimensions.spaceMd)
                    .constrainAs(unlikeMovieButtonRef) {
                        bottom.linkTo(parent.bottom, dimensions.spaceLg)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_thumb_down),
                        contentDescription = null,
                        modifier = Modifier.size(dimensions.spaceXxl)
                    )
                }

                createHorizontalChain(
                    likeMovieButtonRef,
                    spacerRef,
                    unlikeMovieButtonRef,
                    chainStyle = ChainStyle.Packed
                )

                MovieInfo(movie, uiState.videoId, modifier = Modifier.constrainAs(movieInfoRef) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    top.linkTo(likesButtonRef.bottom, dimensions.spaceMd)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(likeMovieButtonRef.top, dimensions.spaceSm)
                })

            }
        }

        ProgressBar(uiState.isLoading)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun MovieInfo(
    movie: MovieUiElement, videoId: String?, modifier: Modifier = Modifier
) {
    val colors = LocalColors.current
    val dimensions = LocalDimensions.current
    val configuration = LocalConfiguration.current

    ConstraintLayout(
        modifier = modifier
    ) {
        val (videoRef, posterRef, titleRef, ratingBarRef, ratingTextRef, genresRef, storylineTitleRef, storylineRef) = createRefs()

        YouTubePlayer(videoId = videoId,
            modifier = Modifier
                .height(dimensions.space4Xl)
                .background(colors.colorSurface)
                .constrainAs(videoRef) {
                    top.linkTo(parent.top, dimensions.spaceXl)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })

        AsyncImage(model = movie.posterPath,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.ic_genre_placeholder),
            error = painterResource(id = R.drawable.ic_genre_placeholder),
            contentScale = ContentScale.Fit,
            modifier = Modifier.constrainAs(posterRef) {
                width = Dimension.value(configuration.screenWidthDp.dp / 3.5f)
                top.linkTo(videoRef.bottom, dimensions.space3Xl)
                start.linkTo(parent.start, dimensions.spaceMd)
            })

        Text(buildAnnotatedString {
            withStyle(style = SpanStyle(color = colors.colorSecondary)) {
                append(movie.title)
            }
            append("  ")
            withStyle(style = SpanStyle(color = colors.colorPrimary)) {
                append(movie.releaseDate)
            }
        }, modifier = Modifier.constrainAs(titleRef) {
            top.linkTo(posterRef.top, dimensions.spaceMd)
            start.linkTo(posterRef.end, dimensions.spaceMd)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        })

        RatingBar(value = movie.voteAverage.toFloat() / 2,
            onValueChange = {},
            onRatingChanged = {},
            config = RatingBarConfig().apply {
                size(dimensions.spaceMd)
                isIndicator(true)
                activeColor(colors.colorPrimary)
                inactiveColor(colors.colorSecondary)
            },
            modifier = Modifier.constrainAs(ratingBarRef) {
                top.linkTo(titleRef.bottom, dimensions.spaceMd)
                start.linkTo(titleRef.start)
            })

        Text(text = "${movie.voteAverage.toFloat()}/10",
            modifier = Modifier.constrainAs(ratingTextRef) {
                centerVerticallyTo(ratingBarRef)
                start.linkTo(ratingBarRef.end, dimensions.spaceMd)
            })

        FlowRow(modifier = Modifier.constrainAs(genresRef) {
            top.linkTo(ratingBarRef.bottom, dimensions.spaceMd)
            start.linkTo(posterRef.end, dimensions.spaceMd)
            end.linkTo(parent.end, dimensions.spaceMd)
            width = Dimension.fillToConstraints
        }) {
            movie.genres.forEach { genre ->
                Chip(onClick = {}) {
                    Text(text = genre)
                }
                Spacer(modifier = Modifier.width(dimensions.spaceXs))
            }
        }

        Text(text = "STORYLINE", modifier = Modifier.constrainAs(storylineTitleRef) {
            top.linkTo(posterRef.bottom, dimensions.spaceLg)
            start.linkTo(parent.start, dimensions.spaceMd)
        })

        Text(
            text = movie.overview,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(storylineRef) {
                top.linkTo(storylineTitleRef.bottom, dimensions.spaceSm)
                start.linkTo(parent.start, dimensions.spaceMd)
                end.linkTo(parent.end, dimensions.spaceMd)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}

@Composable
fun YouTubePlayer(
    videoId: String?,
    modifier: Modifier = Modifier
) {
    val activityLifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    val player = remember { mutableStateOf<YouTubePlayer?>(null) }
    val youtubePlayerView = remember {
        YouTubePlayerView(context).apply {
            activityLifecycle.addObserver(this)
            enableAutomaticInitialization = false
            initialize(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        player.value = youTubePlayer
                    }

                },
                IFramePlayerOptions.Builder().autoplay(1).fullscreen(0).controls(0).build()
            )
        }
    }

    if (videoId != null) {
        player.value?.loadVideo(videoId, 0f)
        player.value?.play()
    }

    AndroidView(
        {
            youtubePlayerView
        }, modifier = modifier
    )
}

@Composable
private fun HandleUiEffects(uiEffect: SharedFlow<MoviesEffect>, navigator: MoovyNavigator) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch {
                uiEffect.collect { uiEffect ->
                    when (uiEffect) {
                        MoviesEffect.NavigateToGenres -> {
                            navigator.navigateToGenres()
                        }
                        MoviesEffect.UnknownError -> {
                            //TODO: show snack bar
                        }
                    }
                }
            }
        }
    }
}