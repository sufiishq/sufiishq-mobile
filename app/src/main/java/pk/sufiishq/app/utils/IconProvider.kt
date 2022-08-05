package pk.sufiishq.app.utils

import pk.sufiishq.app.R
import pk.sufiishq.app.models.MenuIcon

object IconProvider {

    private val iconsMap: Map<String, MenuIcon>

    init {
        val context = app
        iconsMap = buildMap {
            put(
                context.getString(R.string.mark_as_favorite), MenuIcon(
                    drawableId = R.drawable.ic_round_favorite_24,
                    color = MenuIconColors.FAVORITE
                )
            )
            put(
                context.getString(R.string.remove_favorite), MenuIcon(
                    drawableId = R.drawable.ic_round_favorite_border_24,
                    color = MenuIconColors.FAVORITE
                )
            )
            put(
                context.getString(R.string.download_label), MenuIcon(
                    drawableId = R.drawable.ic_round_cloud_download_24,
                    color = MenuIconColors.DOWNLOADS
                )
            )
            put(
                context.getString(R.string.add_to_playlist), MenuIcon(
                    drawableId = R.drawable.ic_round_playlist_add_24,
                    color = MenuIconColors.PLAYLIST
                )
            )
            put(
                context.getString(R.string.delete_label), MenuIcon(
                    drawableId = R.drawable.ic_outline_delete_24,
                    color = MenuIconColors.DELETE
                )
            )
            put(
                context.getString(R.string.split_kalam), MenuIcon(
                    drawableId = R.drawable.ic_round_call_split_24,
                    color = MenuIconColors.SPLIT_KALAM
                )
            )
            put(
                context.getString(R.string.rename_label), MenuIcon(
                    drawableId = R.drawable.ic_outline_edit_24,
                    color = MenuIconColors.RENAME
                )
            )
        }
    }

    fun getIcon(label: String): MenuIcon {
        return iconsMap[label]!!
    }
}