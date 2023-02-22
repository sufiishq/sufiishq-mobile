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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.feature.kalam.controller.KalamController
import pk.sufiishq.app.feature.kalam.helper.TrackListType
import pk.sufiishq.app.feature.kalam.model.Kalam
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.fakeKalam
import pk.sufiishq.app.utils.fakeKalamController
import pk.sufiishq.app.utils.formatDateAs
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIDataRow

const val TEST_TAG_KALAM_DATA_ROW = "test_tag_kalam_data_row"

@PackagePrivate
@Composable
fun KalamItem(
    kalam: Kalam,
    trackListType: TrackListType,
    kalamController: KalamController,
) {
    val isExpanded = rem(false)

    SIDataRow(
        modifier = Modifier.testTag(TEST_TAG_KALAM_DATA_ROW),
        leadingIcon = R.drawable.ic_start_logo,
        trailingIcon = R.drawable.ic_baseline_more_vert_24,
        onTrailingIconClick = { isExpanded.value = !isExpanded.value },
        trailingIconScope = {
            KalamItemPopupMenu(
                isExpanded = isExpanded,
                kalamController = kalamController,
                kalam = kalam,
                trackListType = trackListType,
            )
        },
        onClick = { kalamController.changeTrack(kalam, trackListType) },
        title = kalam.title,
        subTitle = "${kalam.location} ${kalam.recordeDate.formatDateAs(prefix = "- ")}",
    )
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewLight() {
    AuroraLight {
        KalamItem(
            kalam = fakeKalam(),
            trackListType = TrackListType.All(title = optString(R.string.title_all_kalam)),
            kalamController = fakeKalamController(),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun KalamItemPreviewDark() {
    AuroraDark {
        KalamItem(
            kalam = fakeKalam(),
            trackListType = TrackListType.All(title = optString(R.string.title_all_kalam)),
            kalamController = fakeKalamController(),
        )
    }
}
