package pk.sufiishq.aurora.layout

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import kotlinx.coroutines.launch
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.utils.isScrollingUp
import pk.sufiishq.aurora.utils.rem

private val appBarHeight = 52.dp
private val paddingMedium = 12.dp

@Composable
fun <T : Any> SIParallaxLazyColumn(
    @DrawableRes leadingIcon: Int,
    title: String,
    data: List<T>,
    bottomView: @Composable (BoxScope.() -> Unit)? = null,
    noItemText: String? = null,
    compose: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {

    val listData = Pager(PagingConfig(pageSize = Int.MAX_VALUE), pagingSourceFactory = {
        object : PagingSource<Int, T>() {
            override fun getRefreshKey(state: PagingState<Int, T>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
                return LoadResult.Page(data, null, null)
            }
        }
    }).flow

    SIParallaxLazyColumn(
        leadingIcon = leadingIcon,
        title = title,
        data = listData.collectAsLazyPagingItems(),
        bottomView = bottomView,
        noItemText = noItemText,
        compose = compose
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> SIParallaxLazyColumn(
    @DrawableRes leadingIcon: Int,
    title: String,
    data: LazyPagingItems<T>,
    bottomView: @Composable (BoxScope.() -> Unit)? = null,
    noItemText: String? = null,
    compose: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {

    val localDensity = LocalDensity.current
    val containerSize = rem(DpSize.Zero)
    val imageSize = rem(DpSize.Zero)
    val titleSize = rem(DpSize.Zero)
    val headerHeight = rem(0.dp)
    headerHeight.value =
        imageSize.value.height + paddingMedium * 5 + titleSize.value.height + paddingMedium


    val headerHeightPx = with(localDensity) { headerHeight.value.toPx() }
    val appBarHeightPx = with(localDensity) { appBarHeight.toPx() }

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val firstItemTranslationY = remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                val scrollIndex =
                    if (lazyListState.firstVisibleItemScrollOffset <= (with(localDensity) { headerHeight.value.toPx() } / 2)) {
                        0
                    } else 1
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(scrollIndex)
                }
                lazyListState.firstVisibleItemScrollOffset.toFloat()
            } else {
                1000f
            }
        }
    }

    SIBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp)
            .onGloballyPositioned {
                containerSize.value =
                    with(localDensity) { DpSize(it.size.width.toDp(), it.size.height.toDp()) }
            }
    ) {

        val collapseRange: Float = (headerHeightPx - appBarHeightPx)
        val collapseFraction: Float = (firstItemTranslationY.value / collapseRange).coerceIn(0f, 1f)

        SIBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(appBarHeight)
                .align(Alignment.TopStart)
                .graphicsLayer {
                    alpha = collapseFraction
                },
        ) {
            SIRow(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopStart),
                bgColor = AuroraColor.Background,
                radius = 4,
            ) {}
        }

        SIImage(
            modifier = Modifier
                .align(Alignment.TopStart)
                .graphicsLayer {

                    val imageXStart = lerp(
                        (containerSize.value.width / 2) - (imageSize.value.width / 2),
                        imageEnd(imageSize) * -4f,
                        collapseFraction
                    )

                    val imageXEnd = lerp(
                        imageEnd(imageSize) * -4f,
                        imageEnd(imageSize),
                        collapseFraction
                    )

                    val imageX = lerp(
                        imageXStart,
                        imageXEnd,
                        collapseFraction
                    )

                    val imageYStart = lerp(
                        (headerHeight.value / 2) - (imageSize.value.height / 2),
                        (-130f).dp,
                        collapseFraction
                    )

                    val imageYEnd = lerp(
                        0.dp,
                        appBarHeight / 2 - imageSize.value.height / 2,
                        collapseFraction
                    )

                    val imageY = lerp(
                        imageYStart,
                        imageYEnd,
                        collapseFraction
                    )

                    val imageScale = lerp(
                        with(localDensity) { 1.toDp() },
                        (25.dp / obtainUpperSize(imageSize)).toDp(),
                        collapseFraction
                    )

                    translationX = imageX.toPx()
                    translationY = imageY.toPx()
                    scaleX = imageScale.toPx()
                    scaleY = imageScale.toPx()
                }
                .onGloballyPositioned {
                    imageSize.value =
                        with(localDensity) { DpSize(it.size.width.toDp(), it.size.height.toDp()) }
                },
            resId = leadingIcon
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .graphicsLayer {

                    val titleXStart = lerp(
                        (containerSize.value.width / 2) - (titleSize.value.width / 2),
                        paddingMedium * 2 / 10,
                        collapseFraction
                    )

                    val titleXEnd = lerp(
                        paddingMedium * 2 / 10,
                        (paddingMedium * 2).plus(25.dp) * 0.8f,
                        collapseFraction
                    )

                    val titleX = lerp(
                        titleXStart,
                        titleXEnd,
                        collapseFraction
                    )

                    val titleY = lerp(
                        headerHeight.value - (titleSize.value.height + paddingMedium),
                        appBarHeight / 2 - titleSize.value.height / 2,
                        collapseFraction
                    )

                    val textScale = lerp(
                        with(localDensity) { 1.toDp() },
                        with(localDensity) { 0.8f.toDp() },
                        collapseFraction
                    )

                    translationX = titleX.toPx()
                    translationY = titleY.toPx()
                    scaleX = textScale.toPx()
                    scaleY = textScale.toPx()
                }
                .onGloballyPositioned {
                    titleSize.value =
                        with(localDensity) { DpSize(it.size.width.toDp(), it.size.height.toDp()) }
                },
            text = title,
            color = AuroraColor.OnBackground.color(),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontSize = 22.sp,
        )

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .padding(top = appBarHeight + paddingMedium)
                    .fillMaxSize()
            ) {
                val (listViewRef, bottomViewRef) = createRefs()

                SILazyColumn(
                    modifier = Modifier
                        .constrainAs(listViewRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(bottomViewRef.top)
                            height = Dimension.fillToConstraints
                        },
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(0.dp),
                    state = lazyListState,
                    hasItems = data.itemCount > 0,
                    noItemText = noItemText
                ) {

                    item(key = -1) {
                        Spacer(modifier = Modifier.height(headerHeight.value - (appBarHeight)))
                    }

                    itemsIndexed(data) { index, item ->
                        compose(this, index, item!!)
                    }
                }

                bottomView?.let {
                    SIBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(bottomViewRef) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .animateContentSize()
                    ) {
                        if (lazyListState.isScrollingUp()) {
                            SIBox(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .fillMaxWidth()
                            ) {
                                bottomView.invoke(this)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun imageEnd(imageSize: State<DpSize>): Dp {
    return 0.dp - (imageSize.value.width / 2).minus((paddingMedium * 2))
}

private fun obtainUpperSize(size: State<DpSize>): Dp {
    return if (size.value.width > size.value.height) {
        size.value.width
    } else {
        size.value.height
    }
}