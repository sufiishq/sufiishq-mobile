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

import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TileAndroidImage(
    @DrawableRes drawableId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val drawable =
        remember(drawableId) {
            BitmapDrawable(
                context.resources,
                BitmapFactory.decodeResource(
                    context.resources,
                    drawableId,
                ),
            )
                .apply {
                    tileModeX = Shader.TileMode.REPEAT
                    tileModeY = Shader.TileMode.REPEAT
                }
        }
    AndroidView(
        factory = { ImageView(it) },
        update = { imageView -> imageView.background = drawable },
        modifier =
        modifier.semantics {
            this.contentDescription = contentDescription
            role = Role.Image
        },
    )
}
