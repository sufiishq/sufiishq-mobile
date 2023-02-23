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

package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.feature.kalam.controller.KalamController
import pk.sufiishq.app.feature.kalam.controller.KalamViewModel
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.fakeKalamController
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SILazyColumn
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun TracksScreen(
    kalamController: KalamController = hiltViewModel<KalamViewModel>(),
    trackListType: TrackListType,
) {
    kalamController.searchKalam("", trackListType)

    val lazyKalamItems: LazyPagingItems<Kalam> =
        kalamController.getKalamDataFlow().collectAsLazyPagingItems()

    SIColumn { textColor ->
        SearchTextField(
            textColor,
            lazyKalamItems,
            trackListType,
            kalamController,
        )

        SILazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            hasItems = lazyKalamItems.itemCount > 0,
            noItemText = optString(TextRes.dynamic_no_kalam_found, trackListType.title),
        ) {
            items(lazyKalamItems) { track ->
                track?.run {
                    KalamItem(
                        kalam = track,
                        trackListType = trackListType,
                        kalamController = kalamController,
                    )
                }
            }
        }
    }

    // kalam confirm delete dialog
    KalamConfirmDeleteDialog(
        kalamController = kalamController,
    )

    // kalam split dialog
    KalamSplitDialog(
        kalamController = kalamController,
    )

    // kalam download dialog
    KalamDownloadDialog(
        kalamController = kalamController,
    )

    // playlist dialog
    PlaylistDialog(
        kalamController = kalamController,
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun TracksPreviewLight() {
    AuroraLight {
        TracksScreen(
            kalamController = fakeKalamController(),
            trackListType = TrackListType.All(optString(TextRes.title_all_kalam)),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun TracksPreviewDark() {
    AuroraDark {
        TracksScreen(
            kalamController = fakeKalamController(),
            trackListType = TrackListType.All(optString(TextRes.title_all_kalam)),
        )
    }
}
