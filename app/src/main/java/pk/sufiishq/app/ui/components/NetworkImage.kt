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

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox

private enum class State {
    Loading,
    Done,
}

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String,
    contentScale: ContentScale = ContentScale.Fit,
    @DrawableRes placeholder: Int = ImageRes.placeholder,
) {
    val state = rem(State.Loading)
    val context = LocalContext.current

    SIBox {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .placeholder(placeholder)
                .error(placeholder)
                .build(),
            contentDescription = null,
            contentScale = contentScale,

            onLoading = { state.value = State.Loading },
            onError = { state.value = State.Done },
            onSuccess = { state.value = State.Done },
        )

        if (state.value == State.Loading) {
            SIBox(padding = 12) {
                SICircularProgressIndicator(
                    strokeWidth = 2,
                )
            }
        }
    }
}
