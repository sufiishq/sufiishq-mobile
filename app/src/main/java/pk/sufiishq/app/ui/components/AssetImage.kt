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

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import pk.sufiishq.app.utils.assetsToBitmap
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIImage

@Composable
fun AssetImage(
    path: String,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    modifier: Modifier,
    context: Context = LocalContext.current,
) {
    val bitmap = rem(context.assetsToBitmap(path)).value

    bitmap?.asImageBitmap()?.apply {
        SIImage(
            modifier = modifier,
            contentScale = contentScale,
            bitmap = this,
            alignment = alignment,
        )
    }
}
