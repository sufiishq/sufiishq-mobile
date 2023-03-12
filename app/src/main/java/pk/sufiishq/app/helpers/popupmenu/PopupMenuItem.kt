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

package pk.sufiishq.app.helpers.popupmenu

import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor

sealed class PopupMenuItem(
    override val label: String,
    override val resId: Int? = null,
    override val iconTint: AuroraColor? = null,
) : DataMenuItem {

    class MarkAsFavorite(
        itemLabel: String,
        itemResId: Int? = ImageRes.favorite,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class MarkAsNotFavorite(
        itemLabel: String,
        itemResId: Int? = ImageRes.favorite_outline,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Download(
        itemLabel: String,
        itemResId: Int? = ImageRes.download,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class AddToPlaylist(
        itemLabel: String,
        itemResId: Int? = ImageRes.add,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Share(
        itemLabel: String,
        itemResId: Int? = ImageRes.share,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Split(
        itemLabel: String,
        itemResId: Int? = ImageRes.split,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Edit(
        itemLabel: String,
        itemResId: Int? = ImageRes.edit,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Delete(
        itemLabel: String,
        itemResId: Int? = ImageRes.delete,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Facebook(
        itemLabel: String,
        itemResId: Int? = ImageRes.group,
        itemIconTint: AuroraColor? = null,
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)
}
