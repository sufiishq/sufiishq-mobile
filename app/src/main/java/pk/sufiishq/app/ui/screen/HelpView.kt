package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.ui.components.HelpExpandableCard
import pk.sufiishq.app.ui.components.HelpHeader
import pk.sufiishq.app.utils.fakeHelpDataProvider
import pk.sufiishq.app.utils.rem
import pk.sufiishq.app.viewmodels.HelpViewModel
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun HelpView(
    helpDataProvider: HelpDataProvider = hiltViewModel<HelpViewModel>()
) {
    val helpContent = helpDataProvider.getHelpContent().collectAsState(listOf())

    SIColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val lazyListState = rememberLazyListState()

        val firstItemTranslationY = remember {
            derivedStateOf {
                if (lazyListState.firstVisibleItemIndex == 0) {
                    lazyListState.firstVisibleItemScrollOffset.toFloat()
                } else {
                    0f
                }
            }
        }

        val scaleAndVisibility = remember {
            derivedStateOf {
                if (lazyListState.firstVisibleItemIndex == 0) {
                    val imageSize = lazyListState.layoutInfo.visibleItemsInfo[0].size
                    val scrollOffset = lazyListState.firstVisibleItemScrollOffset
                    scrollOffset / imageSize.toFloat()
                } else {
                    0f
                }
            }
        }

        val expandedIndex = rem(0)

        SILazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = lazyListState
        ) {

            item {
                HelpHeader(
                    firstItemTranslationY = firstItemTranslationY,
                    scaleAndVisibility = scaleAndVisibility
                )
            }

            if (helpContent.value.isEmpty()) {
                item {
                    SIBox(modifier = Modifier.fillMaxWidth()) {
                        SICircularProgressIndicator(
                            strokeWidth = 2
                        )
                    }
                }
            }

            itemsIndexed(helpContent.value) { index, item ->
                HelpExpandableCard(
                    expandedIndex = expandedIndex,
                    index = index,
                    item = item,
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewLightHelpView() {
    AuroraLight {
        HelpView(
            helpDataProvider = fakeHelpDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewDarkHelpView() {
    AuroraDark {
        HelpView(
            helpDataProvider = fakeHelpDataProvider()
        )
    }
}