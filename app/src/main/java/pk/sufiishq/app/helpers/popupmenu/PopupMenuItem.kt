package pk.sufiishq.app.helpers.popupmenu

import pk.sufiishq.app.R
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor

sealed class PopupMenuItem(
    override val label: String,
    override val resId: Int? = null,
    override val iconTint: AuroraColor? = null
) : DataMenuItem {

    class MarkAsFavorite(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_favorite_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class MarkAsNotFavorite(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_favorite_border_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Download(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_cloud_download_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class AddToPlaylist(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_playlist_add_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Share(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_share_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Split(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_call_split_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Edit(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_outline_edit_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Delete(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_outline_delete_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Facebook(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_groups_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Help(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_help_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)

    class Theme(
        itemLabel: String,
        itemResId: Int? = R.drawable.ic_round_dark_mode_24,
        itemIconTint: AuroraColor? = null
    ) : PopupMenuItem(itemLabel, itemResId, itemIconTint)
}

