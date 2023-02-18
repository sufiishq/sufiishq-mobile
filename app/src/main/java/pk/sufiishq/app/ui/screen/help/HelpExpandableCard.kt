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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.feature.help.HelpDataType
import pk.sufiishq.app.feature.help.model.HelpContent
import pk.sufiishq.app.ui.components.NetworkImage
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import pk.sufiishq.aurora.widgets.SIExpandableCard

@PackagePrivate
@Composable
fun HelpExpandableCard(
    expandedIndex: MutableState<Int>,
    index: Int,
    item: HelpContent,
) {
    SIExpandableCard(
        header = { textColor ->
            SIIcon(
                resId = R.drawable.ic_baseline_circle_24,
                tint = AuroraColor.Background,
                modifier = Modifier.size(12.dp),
            )
            SIWidthSpace(value = 12)
            SIText(
                text = item.title,
                textColor = textColor,
                fontWeight = FontWeight.Medium,
                textSize = TextSize.Regular,
            )
        },
        isExpanded = expandedIndex.value == index,
        onHeaderClick = { expandedIndex.value = index },
    ) { textColor ->
        item.content.onEach {
            when (it) {
                is HelpDataType.Paragraph ->
                    SIText(
                        text = it.text,
                        textColor = textColor,
                        textSize = TextSize.Small,
                    )
                is HelpDataType.Photo ->
                    NetworkImage(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        url = it.path,
                    )
                is HelpDataType.Divider ->
                    Divider(
                        thickness = it.height.dp,
                        modifier = Modifier.padding(top = 12.dp).testTag("divider_tag"),
                    )
                is HelpDataType.Spacer ->
                    Spacer(
                        modifier = Modifier.height(it.height.dp).testTag("spacer_tag"),
                    )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun LightExpandableCard() {
    AuroraLight {
        HelpExpandableCard(
            expandedIndex = rem(0),
            index = 0,
            item =
            HelpContent(
                "The Title",
                listOf(
                    HelpDataType.Paragraph(buildAnnotatedString { "This is some text" }),
                ),
            ),
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun DarkExpandableCard() {
    AuroraDark {
        HelpExpandableCard(
            expandedIndex = rem(0),
            index = 0,
            item =
            HelpContent(
                "The Title",
                listOf(
                    HelpDataType.Paragraph(buildAnnotatedString { "This is some text" }),
                ),
            ),
        )
    }
}
