package pk.sufiishq.app.helpers

import android.content.Context
import pk.sufiishq.app.R
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor

object PopupMenuItemProvider {

    private var playerPopupMenuItems: List<DataMenuItem>? = null
    private var trackListPopupMenuItems: List<DataMenuItem>? = null
    private var appBarPopupMenuItems: List<DataMenuItem>? = null
    private var playlistPopupMenuItems: List<DataMenuItem>? = null

    fun getPlayerPopupMenuItems(context: Context): List<DataMenuItem> {

        return playerPopupMenuItems ?: listOf(
            PopupMenuItem.MarkAsFavorite(context.getString(R.string.mark_as_favorite)),
            PopupMenuItem.MarkAsNotFavorite(context.getString(R.string.remove_favorite)),
            PopupMenuItem.Download(context.getString(R.string.download_label)),
            PopupMenuItem.AddToPlaylist(context.getString(R.string.add_to_playlist))
        ).also {
            playerPopupMenuItems = it
        }
    }

    fun getTrackListPopupMenuItems(context: Context): List<DataMenuItem> {

        return trackListPopupMenuItems ?: listOf(
            PopupMenuItem.MarkAsFavorite(context.getString(R.string.mark_as_favorite)),
            PopupMenuItem.MarkAsNotFavorite(context.getString(R.string.remove_favorite)),
            PopupMenuItem.Download(context.getString(R.string.download_label)),
            PopupMenuItem.AddToPlaylist(context.getString(R.string.add_to_playlist)),
            PopupMenuItem.Share(context.getString(R.string.share_label)),
            PopupMenuItem.Split(context.getString(R.string.split_kalam)),
            PopupMenuItem.Edit(context.getString(R.string.rename_label)),
            PopupMenuItem.Delete(context.getString(R.string.delete_label))
        ).also {
            trackListPopupMenuItems = it
        }
    }

    fun getAppBarPopupMenuItems(context: Context): List<DataMenuItem> {

        return appBarPopupMenuItems ?: listOf(
            PopupMenuItem.Share(context.getString(R.string.share_label)),
            PopupMenuItem.Facebook(context.getString(R.string.facebook)),
            PopupMenuItem.Help(context.getString(R.string.help)),
            PopupMenuItem.Theme("Theme")
        ).also {
            appBarPopupMenuItems = it
        }
    }

    fun getPlaylistPopupMenuItems(context: Context): List<DataMenuItem> {
        return playlistPopupMenuItems ?: listOf(
            PopupMenuItem.Edit(context.getString(R.string.rename_label)),
            PopupMenuItem.Delete(context.getString(R.string.delete_label))
        ).also {
            playlistPopupMenuItems = it
        }
    }
}


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

