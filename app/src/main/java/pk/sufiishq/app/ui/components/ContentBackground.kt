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

package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun ContentBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SIBox(
        bgColor = AuroraColor.Surface,
        modifier = modifier.fillMaxSize(),
    ) {
        TileAndroidImage(
            modifier = Modifier.fillMaxSize().alpha(0.2f),
            drawableId = ImageRes.pattern,
            contentDescription = "",
        )

        content()
    }
}
