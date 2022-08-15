package pk.sufiishq.app.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import pk.sufiishq.app.R
import pk.sufiishq.app.core.event.dispatcher.EventDispatcher
import pk.sufiishq.app.core.event.events.GlobalEvents
import pk.sufiishq.app.data.providers.HomeDataProvider
import pk.sufiishq.app.helpers.GlobalEventHandler
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.ui.components.buttons.ThemeChangeButton
import pk.sufiishq.app.ui.components.buttons.UpdateButton
import pk.sufiishq.app.utils.MenuIconColors
import pk.sufiishq.app.utils.app
import pk.sufiishq.app.utils.isDarkThem
import pk.sufiishq.app.utils.optValue

@Composable
fun DashboardView(
    navController: NavController,
    homeDataProvider: HomeDataProvider,
    globalEventHandler: GlobalEventHandler,
    eventDispatcher: EventDispatcher
) {

    val matColors = MaterialTheme.colors
    val appConfig = app.appConfig

    val all = stringResource(R.string.all)
    val favorites = stringResource(R.string.favorites)
    val downloads = stringResource(R.string.downloads)
    val playlist = stringResource(R.string.playlist)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(matColors.secondaryVariant)
    ) {

        val (logo, themeChangeButton, btnUpdate, buttonBox) = createRefs()

        val infiniteTransition = rememberInfiniteTransition()
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(15000, easing = LinearEasing)
            )
        )

        Image(
            modifier = Modifier.graphicsLayer {
                rotationY = angle
            }.constrainAs(logo) {
                start.linkTo(parent.start)
                top.linkTo(parent.top, 12.dp)
                end.linkTo(parent.end)
                bottom.linkTo(buttonBox.top, 12.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            colorFilter = ColorFilter.tint(matColors.primary),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )

        // theme change button app only when android version less or equal from Android 9
        ThemeChangeButton(
            modifier = Modifier
                .constrainAs(themeChangeButton) {
                    start.linkTo(parent.start, 12.dp)
                    bottom.linkTo(buttonBox.top, 6.dp)
                }
        )

        UpdateButton(
            show = globalEventHandler.getShowUpdateButton().observeAsState(),
            modifier = Modifier
                .constrainAs(btnUpdate) {
                    start.linkTo(parent.start, 12.dp)
                    top.linkTo(parent.top, 12.dp)
                }
        ) {
            eventDispatcher.dispatch(GlobalEvents.StartUpdateFlow)
        }

        Box(
            modifier = Modifier
                .constrainAs(buttonBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 12.dp)
                }
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    TrackButton(
                        title = all,
                        count = homeDataProvider.countAll().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_check_circle_24,
                        iconColor = MenuIconColors.ALL_KALAM
                    ) {
                        appConfig.trackListType = TrackListType.All()
                        navController.navigate(
                            ScreenType.Tracks.withArgs(
                                ScreenType.Tracks.ALL,
                                all,
                                "0"
                            )
                        )
                    }

                    TrackButton(
                        title = favorites,
                        count = homeDataProvider.countFavorites().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_favorite_border_24,
                        iconColor = MenuIconColors.FAVORITE
                    ) {
                        appConfig.trackListType = TrackListType.Favorites()
                        navController.navigate(
                            ScreenType.Tracks.withArgs(
                                ScreenType.Tracks.FAVORITES,
                                favorites,
                                "0"
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    TrackButton(
                        title = downloads,
                        count = homeDataProvider.countDownloads().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_cloud_download_24,
                        iconColor = MenuIconColors.DOWNLOADS
                    ) {
                        appConfig.trackListType = TrackListType.Downloads()
                        navController.navigate(
                            ScreenType.Tracks.withArgs(
                                ScreenType.Tracks.DOWNLOADS,
                                downloads,
                                "0"
                            )
                        )
                    }

                    TrackButton(
                        title = playlist,
                        count = homeDataProvider.countPlaylist().observeAsState().optValue(0),
                        icon = R.drawable.ic_outline_playlist_play_24,
                        iconColor = MenuIconColors.PLAYLIST
                    ) {
                        navController.navigate(ScreenType.Playlist.route)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackButton(
    title: String,
    count: Int,
    icon: Int,
    iconColor: Color,
    navigate: () -> Unit
) {

    var bgColor = Color(233, 233, 233, 255)
    var textColor = Color(24, 24, 24, 255)

    if (isDarkThem()) {
        bgColor = Color(34, 34, 34, 255)
        textColor = Color(247, 247, 247, 255)
    }

    Box(modifier = Modifier
        .padding(6.dp)
        .clip(RoundedCornerShape(5.dp))
        .clickable {
            navigate()
        }
    ) {
        Box(
            modifier = Modifier
                .background(bgColor)
                .padding(20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Icon(
                    modifier = Modifier.padding(10.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = iconColor
                )

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 30.sp,
                        color = textColor,
                        fontFamily = FontFamily.Serif,
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = title,
                        color = textColor,
                    )
                }
            }
        }
    }
}