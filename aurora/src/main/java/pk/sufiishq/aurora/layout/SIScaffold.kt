package pk.sufiishq.aurora.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    drawer: @Composable (ColumnScope.() -> Unit)? = null,
    drawerGesturesEnabled: Boolean = true,
    content: @Composable (fgColor: AuroraColor) -> Unit
) {

    val mBgColor = bgColor.validateBackground()
    val mFgColor = bgColor.getForegroundColor()

    var drawerContent: (@Composable ColumnScope.() -> Unit)? = null
    if (drawer != null) {
        drawerContent = {
            SIColumn(
                bgColor = AuroraColor.Background,
                modifier = Modifier.width(280.dp)
            ) {
                drawer()
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = snackbarHost,
        topBar = { topBar(mFgColor) },
        bottomBar = { bottomBar(mFgColor) },
        drawerShape = object : Shape {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density
            ): Outline {
                val width = with(density) { 280.dp.toPx() }
                return Outline.Rectangle(Rect(0f, 0f, width /* width */, size.height /* height */))
            }
        },
        drawerContent = drawerContent,
        drawerGesturesEnabled = drawerGesturesEnabled,
        floatingActionButton = {
            onFloatingButtonAction?.let {

                val density = LocalDensity.current
                AnimatedVisibility(
                    modifier = Modifier,
                    visible = isVisibleFAB,
                    enter = slideInVertically {
                        with(density) { 40.dp.roundToPx() }
                    } + fadeIn(),
                    exit = slideOutVertically {
                        with(density) { 40.dp.roundToPx() }
                    } + fadeOut(
                        animationSpec = keyframes {
                            this.durationMillis = 120
                        }
                    )
                ) {
                    FloatingActionButton(
                        onClick = it,
                        backgroundColor = AuroraColor.SecondaryVariant.color()
                    ) {
                        Icon(
                            tint = AuroraColor.SecondaryVariant.getForegroundColor().color(),
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        backgroundColor = mBgColor.color()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content(mFgColor)
        }
    }
}