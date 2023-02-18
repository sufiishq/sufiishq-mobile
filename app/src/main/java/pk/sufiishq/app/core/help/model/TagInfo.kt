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

package pk.sufiishq.app.core.help.model

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

data class TagInfo(
    val first: Int = 0,
    val last: Int = 0,
    val style: String = "",
) {
    fun getStyle(): SpanStyle {
        return when (style) {
            "bold" -> SpanStyle(fontWeight = FontWeight.Bold)
            "italic" -> SpanStyle(fontStyle = FontStyle.Italic)
            else -> SpanStyle()
        }
    }
}
