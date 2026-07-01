package pk.sufiishq.aurora.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor
import pk.sufiishq.aurora.theme.validateBackground

@Composable
fun SIScaffold(
    bgColor: AuroraColor = AuroraColor.Transparent,
    topBar: @Composable (fgColor: AuroraColor) -> Unit = {},
    bottomBar: @Composable (fgColor: AuroraColor) -> Unit = {},
    onFloatingButtonAction: (() -> Unit)? = null,
    isVisibleFAB: Boolean = true,
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    snackbarHost: @Composable (SnackbarHostState) -> Unit = {
        SnackbarHost(it)
    },
    drawer: (@Composable ColumnScope.() -> Unit)? = null,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    drawerGesturesEnabled: Boolean = true,
    content: @Composable (fgColor: AuroraColor) -> Unit
) {

    val mBgColor = bgColor.validateBackground()
    val mFgColor = bgColor.getForegroundColor(bgColor.color())


    val scaffoldContent: @Composable () -> Unit = {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                snackbarHost(snackbarHostState)
            },

            topBar = {
                topBar(mFgColor)
            },

            bottomBar = {
                bottomBar(mFgColor)
            },

            floatingActionButton = {

                onFloatingButtonAction?.let { action ->

                    AnimatedVisibility(
                        visible = isVisibleFAB,
                        enter = slideInVertically {
                            40
                        } + fadeIn(),

                        exit = slideOutVertically {
                            40
                        } + fadeOut()
                    ) {

                        FloatingActionButton(
                            onClick = action,
                            backgroundColor =
                                AuroraColor.SecondaryContainer.color()
                        ) {

                            Icon(
                                tint =
                                    AuroraColor.SecondaryContainer
                                        .getForegroundColor(
                                            bgColor.color()
                                        )
                                        .color(),

                                imageVector = Icons.Filled.Add,

                                contentDescription = null
                            )
                        }
                    }
                }
            },

            containerColor = mBgColor.color()

        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    //.imePadding()
            ) {
                content(mFgColor)
            }
        }
    }


    if (drawer != null) {

        ModalNavigationDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerState = drawerState,

            gesturesEnabled = drawerGesturesEnabled,

            drawerContent = {

                SIColumn(
                    bgColor = AuroraColor.Background,
                    modifier = Modifier.width(280.dp)
                ) {
                    drawer()
                }
            }
        ) {
            scaffoldContent()
        }
    } else {
        scaffoldContent()
    }
}