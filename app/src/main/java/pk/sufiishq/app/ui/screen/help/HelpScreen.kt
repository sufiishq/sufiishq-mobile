/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.ui.screen.help

import androidx.compose.foundation.layout.Arrangement
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
import pk.sufiishq.app.core.help.controller.HelpController
import pk.sufiishq.app.core.help.controller.HelpViewModel
import pk.sufiishq.app.utils.fakeHelpController
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun HelpScreen(
    helpController: HelpController = hiltViewModel<HelpViewModel>(),
) {
    val helpContent = helpController.getHelpContent().collectAsState(listOf())

    SIColumn(
        modifier = Modifier.fillMaxSize(),
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = lazyListState,
        ) {
            item {
                HelpHeader(
                    firstItemTranslationY = firstItemTranslationY,
                    scaleAndVisibility = scaleAndVisibility,
                )
            }

            if (helpContent.value.isEmpty()) {
                item {
                    SIBox(modifier = Modifier.fillMaxWidth()) {
                        SICircularProgressIndicator(
                            strokeWidth = 2,
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
        HelpScreen(
            helpController = fakeHelpController(),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewDarkHelpView() {
    AuroraDark {
        HelpScreen(
            helpController = fakeHelpController(),
        )
    }
}
