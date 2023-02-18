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

package pk.sufiishq.app.feature.admin.highlight

import androidx.annotation.DrawableRes
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.getString
import pk.sufiishq.aurora.theme.AuroraColor

sealed class HighlightStatus(
    val bgColor: AuroraColor,
    @DrawableRes val leadingIcon: Int,
    val label: String,
) {
    class OnGoing(
        bgColor: AuroraColor = AuroraColor.Green,
        leadingIcon: Int = R.drawable.round_error_outline_24,
        label: String = getString(R.string.msg_highlight_ongoing),
    ) : HighlightStatus(bgColor, leadingIcon, label)

    class Expired(
        bgColor: AuroraColor = AuroraColor.OnError,
        leadingIcon: Int = R.drawable.round_error_outline_24,
        label: String = getString(R.string.msg_highlight_expired),
    ) : HighlightStatus(bgColor, leadingIcon, label)
}
